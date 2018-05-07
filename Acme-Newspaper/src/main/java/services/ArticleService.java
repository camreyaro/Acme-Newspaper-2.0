
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Transactional;

import org.apache.commons.validator.routines.UrlValidator;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ArticleRepository;
import security.LoginService;
import domain.Article;
import domain.FollowUp;
import domain.Newspaper;
import domain.SpamWord;
import domain.User;

@Service
@Transactional
public class ArticleService {

	@Autowired
	private ArticleRepository	articleRepository;

	// Servicios
	@Autowired
	private NewspaperService	newspaperService;
	@Autowired
	private SpamWordService		spamWordService;
	@Autowired
	private FollowUpService		followUpService;
	@Autowired
	private ActorService		actorService;
	
	@Autowired
	Validator						validator;


	public Article findOne(int id) {
		Article res = this.articleRepository.findOne(id);
		return res;
	}

	public Collection<Article> findAll() {
		return this.articleRepository.findAll();
	}

	public Collection<Article> articlesByUserId(int id) {
		return this.articleRepository.getAllArticlesByUserId(id);
	}
	
	
	// --- CREATE ---
	public Article create(Newspaper n) {
		Assert.notNull(n);
		Assert.isTrue(!n.getPublished());
		Assert.notNull(this.actorService.findByPrincipal());
		domain.User creator = (User) this.actorService.findByPrincipal();
		Article a = new Article();
		a.setNewspaper(n);
		a.setPictureURLs(new ArrayList<String>());
		a.setCreator(creator);
		return a;
	}
	
	
	// --- SAVE ---
	public Article save(Article a) {
		Assert.notNull(a.getNewspaper());
		Assert.notNull(a.getTitle());
		Assert.notNull(a.getBody());
		Assert.isTrue(!a.getTitle().isEmpty());
		Assert.isTrue(!a.getBody().isEmpty());
		Assert.isTrue(!a.getNewspaper().getPublished());
		Assert.isTrue(a.getCreator().equals(this.actorService.findByPrincipal()) || a.getNewspaper().getPublisher().equals(this.actorService.findByPrincipal()));

		Article saved;
		
		if(a.getPictureURLs() != null){
			
			UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
			
			for(String url : a.getPictureURLs()){
				Assert.isTrue(urlValidator.isValid(url), "org.hibernate.validator.constraints.URL.message");
			}
		}

		if (a.getId() == 0) {
			Newspaper newspaper = a.getNewspaper();

			saved = this.articleRepository.save(a);

			Collection<Article> articles = a.getNewspaper().getArticles();
			articles.add(saved);
			newspaper.setArticles(articles);
			newspaper = this.newspaperService.saveForArticle(newspaper);

		} else
			saved = this.articleRepository.save(a);

		return saved;

	}

	//Delete admin

	public void delete(Article a) {
		Assert.notNull(a);
		Collection<FollowUp> followUps;
		Assert.isTrue(LoginService.getPrincipal().isAuthority("ADMIN"));
		followUps = this.followUpService.getFollowUpsFromArticle(a.getId());

		for (FollowUp f : new ArrayList<>(followUps))
			this.followUpService.delete(f);

		a.getNewspaper().getArticles().remove(a);
		this.articleRepository.delete(a);
	}
	
	
	
	// --- RECONSTRUCT ---
	public Article reconstruct(Article a, BindingResult binding) {
		Article result;
		final Article original = this.articleRepository.findOne(a.getId());
		
		if (a.getId() == 0) {
			result = a;
			result.setCreator((User) this.actorService.findByPrincipal());
			if(a.getSaved()==null)
				a.setSaved(false);
		} else {
			//Aquí van los atributos del formulario
			result = a;
			result.setNewspaper(original.getNewspaper());
			result.setCreator(original.getCreator());
		}
		this.validator.validate(result, binding);

		return result;

	}
	//Other business methods

	public Collection<Article> getArticlesWithSpamWords() {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("Acme-Newspaper");

		EntityManager em = factory.createEntityManager();
		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
		em.getTransaction().begin();

		String regexp = "";
		for (SpamWord sp : this.spamWordService.findAll())
			regexp += sp.getWord() + "|";

		QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Article.class).get();
		org.apache.lucene.search.Query luceneQuery = qb.keyword().onFields("title", "summary", "body", "pictureURLs").ignoreFieldBridge().matching(regexp).createQuery();

		javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Article.class);

		List result = jpaQuery.getResultList();
		Set<Article> cc = new HashSet<>(result);

		em.getTransaction().commit();
		em.close();

		return cc;
	}
	
	public Collection<Article> getPublishedArticlesByUserId(int id){
		Assert.notNull(id);
		return this.articleRepository.getPublishedArticlesByUserId(id);
	}
	
	public Collection<Article> getPrivatePublishedArticlesByCustomerId(int id){
		Assert.notNull(id);
		return this.articleRepository.getPrivatePublishedArticlesByCustomerId(id);
	}
	
	public Double avgFollowUpsPerArticle(){
		return this.articleRepository.avgFollowUpsPerArticle();
	}
	
	public Collection<Article> findAllValidAndPublic(){
		return this.articleRepository.findAllValidAndPublic();
	}
	
	
	public Collection<Article> findPublicArticlesByKeyword(String keyword){
		return this.articleRepository.findAllValidAndPublicByKeyword(keyword);
	}
	
	public Collection<Article> findSuscriptedArticlesByKeyword(String keyword, int actorId){
		return this.articleRepository.findSuscriptedArticlesByKeyword(keyword, actorId);
	}

	public Collection<String> getURLsDeUnArticleId(Integer id) {
		// TODO Auto-generated method stub
		return this.articleRepository.getURLsDeUnArticleId(id);
	}
	
	public Collection<Article> getPrivatePublishedNotSuscribedArticlesByUserId(int userId){
		return this.articleRepository.getPrivatePublishedNotSuscribedArticlesByUserId(userId, this.actorService.findByPrincipal().getId());
	}
	
	public Collection<Article> getPrivatePublishedSuscribedArticlesByUserId(int userId){
		return this.articleRepository.getPrivatePublishedSuscribedArticlesByUserId(userId, this.actorService.findByPrincipal().getId());
	}
	
	public Collection<Article> getAllPrivatePublishedArticlesByUserId(int userId){
		return this.articleRepository.getAllPrivatePublishedArticlesByUserId(userId);
	}

	public Collection<Article> findAdminByKeyword(String s) {
		return this.articleRepository.findAdminByKeyword(s);
	}

	public Collection<Article> getArticlesOfNewspaperId(int id) {
		return this.articleRepository.getArticlesOfNewspaperId(id);
	}
	
	//Paginated repository
	public Page<Article> getArticlesByUserIdPaginate(final Integer pageNumber,
			final Integer pageSize, int id) {
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		return this.articleRepository.getAllArticlesByUserIdPaginate(id, request);
	}
	
	public Page<Article> findAllValidAndPublicPaginate(final Integer pageNumber,
			final Integer pageSize){
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		return this.articleRepository.findAllValidAndPublicPaginate(request);
	}
	
	public Page<Article> findAdminByKeywordPaginate(final Integer pageNumber,
			final Integer pageSize, String s) {
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		return this.articleRepository.findAdminByKeywordPaginate(s, request);
	}
	
	public Page<Article> findPublicArticlesByKeywordPaginate(final Integer pageNumber,
			final Integer pageSize, String keyword){
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		return this.articleRepository.findAllValidAndPublicByKeyword(keyword, request);
	}
	
	public Page<Article> findSuscriptedArticlesByKeywordPaginate(final Integer pageNumber,
			final Integer pageSize,String keyword, int actorId){
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		return this.articleRepository.findSuscriptedArticlesByKeywordPaginate(keyword, actorId, request);
	}

}
