package controllers;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ArticleService;
import services.FollowUpService;
import services.SuscriptionService;
import domain.Article;
import domain.FollowUp;
import domain.User;

@Controller
@RequestMapping("/newspaper/article/followup")
public class FollowUpController extends AbstractController{

	@Autowired
	ArticleService articleService;
	
	@Autowired
	FollowUpService followUpService;
	
	@Autowired
	ActorService actorService;
	
	@Autowired
	SuscriptionService suscriptionService;

	// Constructors -----------------------------------------------------------

	public FollowUpController() {
		super();
	}
	
	@RequestMapping("/user/create")
	public ModelAndView create(@RequestParam String articleId) {
		ModelAndView res;
		Integer id = new Integer(articleId);
		Article a = this.articleService.findOne(id);

		FollowUp f = this.followUpService.create(a);

		if (!a.getSaved() || !a.getCreator().equals(this.actorService.findByPrincipal()))
			res = new ModelAndView("welcome/index");
		else {

			res = new ModelAndView("newspaper/article/followup/user/create");
			res.addObject("followUp", f);
			res.addObject("articleId", a.getId());
		}

		return res;
	}
	
	
	@RequestMapping(value = "/user/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(FollowUp followUp, BindingResult binding) {
		ModelAndView res;
		FollowUp foll = this.followUpService.reconstruct(followUp, binding);
		
		if (binding.hasErrors()){
			res = this.createEditModelAndView(foll);
		}else
			try {
				FollowUp toSave = foll;
				
				toSave.setId(0);
				toSave.setMoment(new Date(System.currentTimeMillis() - 100));
				this.followUpService.save(toSave);

				Article art = toSave.getArticle();
				
				res = new ModelAndView("redirect:/newspaper/article/followup/list.do");
				res.addObject("articleId", foll.getArticle().getId() );
				res.addObject("followUps", this.followUpService.getFollowUpsFromArticle(art.getId()));
				//res.addObject("articled", rend.getId());
			} catch (Throwable oops) {
				String messageCode = "followUp.commit.error";
				if (oops.getMessage().contains("org.hibernate.validator.constraints.URL.message"))
					messageCode = "org.hibernate.validator.constraints.URL.message";
				res = this.createEditModelAndView(foll, messageCode);

			}	
				
		return res;
	}
	
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value = "articleId", required = true) String articleId) {
		ModelAndView res;
		Integer id = new Integer(articleId);
		Article a = this.articleService.findOne(id);
		String newspaperId=  String.valueOf(a.getNewspaper().getId());
		Boolean createFU = false;
		
		try{
			User actual = (User) this.actorService.findByPrincipal();
			createFU = a.getCreator().getId() == actual.getId();
		}catch(Throwable oops){}
		
		if (!a.getSaved())
			res = new ModelAndView("welcome/index");
		else {
			if(a.getNewspaper().getPublished()){
				if(a.getNewspaper().getPublicNp()){
					Collection<FollowUp> followUps = this.followUpService.getFollowUpsFromArticle(id);
					res = new ModelAndView("newspaper/article/followup/list");
					res.addObject("followUps", followUps);
					res.addObject("articleId", articleId);
					res.addObject("createFU", createFU);
				}else{
					if(this.suscriptionService.isCustomerSuscribe(newspaperId) || createFU){
						Collection<FollowUp> followUps = this.followUpService.getFollowUpsFromArticle(id);
						res = new ModelAndView("newspaper/article/followup/list");
						res.addObject("followUps", followUps);
						res.addObject("articleId", articleId);
						res.addObject("createFU", createFU);
					}else{
						res = new ModelAndView("welcome/index");
					}
				}
			}else{
				res = new ModelAndView("welcome/index");
			}
		}

		return res;
	}
	
	
	// Ancillary methods
		protected ModelAndView createEditModelAndView(FollowUp f) {
			return this.createEditModelAndView(f, null);
		}

		protected ModelAndView createEditModelAndView(FollowUp f, String msg) {
			ModelAndView res;
			res = new ModelAndView("newspaper/article/followup/user/create");
			
			res.addObject("followUp", f);
			res.addObject("articleId", f.getArticle().getId());
			res.addObject("message", msg);
			
			return res;
		}
}
