package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FollowUpRepository;
import domain.Article;
import domain.FollowUp;
import domain.User;

@Service
@Transactional
public class FollowUpService {

	@Autowired
	private FollowUpRepository followUpRepository;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private ActorService actorService;
	
	@Autowired
	Validator						validator;

	// --- REPOSITORY ---
	public Collection<FollowUp> getFollowUpsFromArticle(int articleId) {
		return this.followUpRepository.getFollowUpsFromArticle(articleId);
	}

	// --- CREATE ---
	public FollowUp create(Article a) {
		Assert.notNull(a);
		Assert.isTrue(a.getSaved());
		Assert.isTrue(a.getCreator()
				.equals(this.actorService.findByPrincipal()));
		Assert.isTrue(a.getNewspaper().getPublished());

		FollowUp f = new FollowUp();
		f.setArticle(a);
		f.setPictureUrls(new ArrayList<String>());

		return f;
	}

	// --- SAVE ---
	public FollowUp save(FollowUp f) {
		Assert.notNull(f.getArticle());
		Assert.isTrue(f.getArticle().getSaved());
		Assert.isTrue(f.getArticle().getNewspaper().getPublished());
		Assert.notNull(f.getTitle());
		Assert.notNull(f.getText());
		Assert.isTrue(!f.getTitle().isEmpty());
		Assert.isTrue(!f.getText().isEmpty());
		
		
		User actual = (User) this.actorService.findByPrincipal();
		
		Assert.isTrue(f.getArticle().getCreator()
				.equals(actual));
		FollowUp saved;
		
		if(f.getPictureUrls() != null){
			
			UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
			
			for(String url : f.getPictureUrls()){
				Assert.isTrue(urlValidator.isValid(url), "org.hibernate.validator.constraints.URL.message");
			}
		}

		if (f.getId() == 0) {

			f.setMoment(new Date(System.currentTimeMillis() - 200));
			saved = this.followUpRepository.save(f);
		} else
			saved = this.followUpRepository.save(f);

		return saved;

	}
	
	// --- RECONSTRUCT ---
	public FollowUp reconstruct(FollowUp f, BindingResult binding) {
		FollowUp result;

		if (f.getId() == 0) {
			result = f;
		} else {
			result = this.followUpRepository.findOne(f.getId());
			Assert.notNull(result);
			//Aquí van los atributos del formulario
			result.setPictureUrls(f.getPictureUrls());
			result.setText(f.getText());
			result.setTitle(f.getTitle());
		}
		this.validator.validate(result, binding);

		return result;

	}
	
	//DELETE
	
	public void delete(FollowUp followUp){
		Assert.notNull(followUp);

		followUpRepository.delete(followUp);
	}

	public FollowUp findOne(int entityId) {
		return this.followUpRepository.findOne(entityId);
	}

	// DASHBOARD
	public Collection<FollowUp> getFollowUpsPerArticleUpOneWeek() {
		return this.followUpRepository.getFollowUpsPerArticleUpOneWeek();
	}

	public Collection<FollowUp> getFollowUpsPerArticleUpTwoWeek() {
		return this.followUpRepository.getFollowUpsPerArticleUpTwoWeek();
	}
}
