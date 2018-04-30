
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.NewspaperRepository;
import security.LoginService;
import domain.Article;
import domain.Newspaper;
import domain.SpamWord;
import domain.Suscription;
import domain.User;
import domain.Volumen;

@Service
@Transactional
public class NewspaperService {

	// Managed repository ------------------------------ (Relacion con su propio repositorio)
	@Autowired
	private NewspaperRepository	newspaperRepository;
	@Autowired
	private SpamWordService		spamWordService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private UserService			userService;

	@Autowired
	private ArticleService		articleService;
	@Autowired
	private SuscriptionService	suscriptionService;
	@Autowired
	private VolumenService		volumenService;

	@Autowired
	Validator					validator;


	// Simple CRUD methods ------------------------------ (Operaciones básicas, pueden tener restricciones según los requisitos)
	public Newspaper create() {
		final Newspaper newspaper = new Newspaper();
		newspaper.setPublisher((User) this.actorService.findByPrincipal());
		newspaper.setPublished(false);
		newspaper.setArticles(new ArrayList<Article>());
		return newspaper;
	}

	public Collection<Newspaper> findAll() {
		Collection<Newspaper> newspapers;

		newspapers = this.newspaperRepository.findAll();
		Assert.notNull(newspapers, "error.commit.null");

		return newspapers;
	}

	public Newspaper findOne(final int NewspaperId) {
		Newspaper newspaper;
		newspaper = this.newspaperRepository.findOne(NewspaperId);
		Assert.notNull(newspaper, "error.commit.null");

		return newspaper;
	}

	public Newspaper save(final Newspaper newspaper) {
		final User user = (User) this.actorService.findByPrincipal();
		Assert.isTrue(LoginService.getPrincipal().isAuthority("USER"), "error.commit.owner");
		Assert.isTrue(user.getUserAccount().equals(newspaper.getPublisher().getUserAccount()), "error.commit.owner");
		Assert.isTrue(newspaper.getPublicNp() || (!newspaper.getPublicNp() && newspaper.getPrice() > 0.0), "newspaper.error.price");
		Newspaper result;

		if (newspaper.getPublicNp())
			newspaper.setPrice(0.0);

		if (newspaper.getId() == 0) {
			result = this.newspaperRepository.save(newspaper);

			//¿Esto hay que hacerlo o el nuevo newspaper se añade automaticamente cuando guardamos por primera vez?
			final Collection<Newspaper> newspapers = user.getNewspapers();
			newspapers.add(result);
			user.setNewspapers(newspapers);
			this.userService.save(user);

		} else
			result = this.newspaperRepository.save(newspaper);

		Assert.notNull(result, "error.commit.null");
		return result;
	}

	public Newspaper publish(final Newspaper newspaper) {
		Assert.notNull(newspaper, "error.commit.null");
		Assert.isTrue(this.findNotSavedArticlesByNewspaper(newspaper.getId()) == 0 && newspaper.getArticles().size() > 0 && !newspaper.getPublished(), "error.commit.publish");
		Newspaper result;

		final Date publicationDate = new Date();
		newspaper.setPublicationDate(publicationDate);
		for (final Article a : newspaper.getArticles()) {
			a.setMoment(publicationDate);
			this.articleService.save(a);
		}
		newspaper.setPublished(true);
		result = this.newspaperRepository.save(newspaper);
		return result;

	}
	public void delete(final Newspaper newspaper) {
		Assert.notNull(newspaper, "error.commit.null");
		Assert.isTrue(LoginService.getPrincipal().isAuthority("ADMIN"), "error.commit.permission");

		newspaper.getPublisher().getNewspapers().remove(newspaper);

		for (final Article a : new ArrayList<>(newspaper.getArticles())) {
			newspaper.getArticles().remove(a);
			this.articleService.delete(a);
		}

		for (final Suscription s : new ArrayList<>(this.suscriptionService.suscriptionByNewspaperId(newspaper.getId())))
			this.suscriptionService.delete(s.getId());

		for (final Volumen v : this.volumenService.getVolumensOfNewspaper(newspaper.getId()))
			this.volumenService.removeNewspaper(v, newspaper);
		this.newspaperRepository.delete(newspaper);
	}

	// Other bussines methods ------------------------------ (Otras reglas de negocio, como por ejemplo findRegisteredUser())

	public Integer findNotSavedArticlesByNewspaper(final int newspaperId) {
		return this.newspaperRepository.findNotSavedArticlesByNewspaper(newspaperId);
	}

	public Collection<Newspaper> findAllPublished() {
		return this.newspaperRepository.findAllPublished();
	}

	public Collection<Newspaper> findNewspapersByKeyword(final String keyword) {
		return this.newspaperRepository.findByKeyword(keyword.replaceAll("[^a-zA-Z0-9_.ÑñáéíóúÁÉÍÓÚ ]", ""));
	}

	public Collection<Newspaper> findAllByUser(final int userId) {
		return this.newspaperRepository.findAllByUser(userId);
	}

	public Collection<Newspaper> getNewspapersWithSpamWords() {

		final EntityManagerFactory factory = Persistence.createEntityManagerFactory("Acme-Newspaper");

		final EntityManager em = factory.createEntityManager();
		final FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
		em.getTransaction().begin();

		String regexp = "";
		for (final SpamWord sp : this.spamWordService.findAll())
			regexp += sp.getWord() + "|";

		final QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Newspaper.class).get();
		final org.apache.lucene.search.Query luceneQuery = qb.keyword().onFields("title", "description", "pictureURLs").ignoreFieldBridge().matching(regexp).createQuery();

		final javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Newspaper.class);

		final List result = jpaQuery.getResultList();
		final Set<Newspaper> cc = new HashSet<>(result);

		em.getTransaction().commit();
		em.close();

		return cc;
	}

	public Newspaper saveForArticle(final Newspaper newspaper) {
		return this.newspaperRepository.save(newspaper);
	}

	public void saveAndFlush(final Newspaper newspaper) {
		this.newspaperRepository.saveAndFlush(newspaper);
	}

	// DASHBOARD

	public Double avgArticlesByNewspaper() {
		return this.newspaperRepository.avgArticlesByNewspaper();
	}

	public Double stddevArticlesByNewspaper() {
		return this.newspaperRepository.stddevArticlesByNewspaper();
	}

	public Collection<Newspaper> getNewspapersUpper10PerCentArticles() {
		return this.newspaperRepository.getNewspapersUpper10PerCentArticles();
	}

	public Collection<Newspaper> getNewspapersLower10PerCentArticles() {
		return this.newspaperRepository.getNewspapersLower10PerCentArticles();
	}

	public Double ratioPublicVsPrivateNewspapers() {
		return this.newspaperRepository.ratioPublicVsPrivateNewspapers();
	}

	public Double avgArticlesPerPrivateNewspapers() {
		return this.newspaperRepository.avgArticlesPerPrivateNewspapers();
	}

	public Double avgArticlesPerPublicNewspapers() {
		return this.newspaperRepository.avgArticlesPerPublicNewspapers();
	}

	public Double ratioSuscribersPerPrivateNewspapersVsCustomers() {
		return this.newspaperRepository.ratioSuscribersPerPrivateNewspapersVsCustomers();
	}

	public Double avgRatioPublicVsPrivateNewspapers() {
		return this.newspaperRepository.avgRatioPublicVsPrivateNewspapers();
	}

	public Collection<Newspaper> findAllAvaibles() {
		return this.newspaperRepository.findAllAvaibles();
	}

	public Newspaper reconstruct(final Newspaper n, final BindingResult binding) {
		Newspaper result;
		final Newspaper original = this.newspaperRepository.findOne(n.getId());

		if (n.getId() == 0) {
			result = n;
			result.setPublisher((User) this.actorService.findByPrincipal());
			result.setPublished(false);
			result.setArticles(new ArrayList<Article>());

		} else {
			result = n;
			result.setTitle(n.getTitle());
			result.setDescription(n.getDescription());
			result.setPictureUrl(n.getPictureUrl());
			result.setPublicNp(n.getPublicNp());
			result.setPrice(n.getPrice());

			result.setPublisher(original.getPublisher());
			result.setArticles(original.getArticles());
			result.setPublicationDate(original.getPublicationDate());
			result.setPublished(original.getPublished());
		}
		this.validator.validate(result, binding);

		return result;

	}
	
	//Paginated repository
	public Page<Newspaper> getPublishedNewspapersPaginate(final Integer pageNumber,
			final Integer pageSize) {
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		return this.newspaperRepository.getPublishedNewspapersPaginate(request);
	}
	
	public Page<Newspaper> getNewspapersByKeywordPaginate(final Integer pageNumber,
			final Integer pageSize, String keyword) {
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		return this.newspaperRepository.findByKeywordPaginate(keyword, request);
	}
	
	public Page<Newspaper> findAllByUserPaginate(final Integer pageNumber,
			final Integer pageSize, final int userId) {
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		return this.newspaperRepository.findAllByUserPaginate(userId, request);
	}

}
