
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.AdvertisementService;
import services.ArticleService;
import services.NewspaperService;
import services.SuscriptionService;
import domain.Advertisement;
import domain.Agent;
import domain.Article;
import domain.Newspaper;

@Controller()
@RequestMapping("/advertisement/agent")
public class AdvertisementAgentController extends AbstractController {

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private SuscriptionService		suscriptionService;

	@Autowired
	private AdvertisementService	advertisementService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private ArticleService			articleService;


	// Listing ----------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Advertisement> advertisements;
		Agent agent = (Agent) this.actorService.findByPrincipal();

		advertisements = this.advertisementService.findAdvertisementsByAgent(agent.getId());

		result = new ModelAndView("advertisement/agent/list");
		result.addObject("advertisements", advertisements);
		result.addObject("requestURI", "advertisement/agent/list.do");
		return result;
	}

	@RequestMapping("/administrator/delete")
	public ModelAndView deleteNewspaper(@RequestParam(value = "newspaperId", required = true) int newspaperId) {
		ModelAndView result;
		Newspaper newspaper;

		newspaper = this.newspaperService.findOne(newspaperId);

		try {
			this.newspaperService.delete(newspaper);
		} catch (Throwable o) {
			return new ModelAndView("redirect:/newspaper/display.do?newspaperId=" + newspaperId);
		}

		result = new ModelAndView("redirect:/newspaper/list.do");

		return result;
	}

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(value = "newspaperId", required = true) String newspaperId) {
		ModelAndView result;
		Newspaper newspaper;
		Collection<Article> articles = new ArrayList<Article>();
		Integer id;
		Boolean owner, suscribe;
		Boolean esAdmin = false;

		try {
			esAdmin = LoginService.getPrincipal().isAuthority("ADMIN");
		} catch (Throwable oops) {
		}

		try {
			id = Integer.valueOf(newspaperId);
			newspaper = this.newspaperService.findOne(id);
			articles = this.articleService.getArticlesOfNewspaperId(newspaper.getId());
		} catch (Throwable o) { //peta en el caso de que meta una id no valida, redireccionamos a lista de newspaper
			return new ModelAndView("redirect:list.do");
		}

		try {
			owner = newspaper.getPublisher().getUserAccount().equals(LoginService.getPrincipal()); //si peta porque es noLogueado
			suscribe = this.suscriptionService.isCustomerSuscribe(newspaperId);
		} catch (Throwable o) { //Si no esta logueado, este try peta y por lo tanto el user no es owner ni suscrito.
			owner = false;
			suscribe = false;
		}

		if (!esAdmin && !owner && !newspaper.getPublished()) //si intenta acceder a un periodico no valido
			return new ModelAndView("redirect:list.do");

		result = new ModelAndView("newspaper/display");
		result.addObject("articles", articles);
		result.addObject("newspaper", newspaper);
		result.addObject("owner", owner);
		result.addObject("notSavedArticles", this.newspaperService.findNotSavedArticlesByNewspaper(id));
		result.addObject("suscribe", suscribe);
		return result;
	}

	protected ModelAndView createEditModelAndView(Newspaper newspaper) {
		return this.createEditModelAndView(newspaper, null);
	}

	protected ModelAndView createEditModelAndView(Newspaper newspaper, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("newspaper/user/edit");
		result.addObject("newspaper", newspaper);
		result.addObject("message", messageCode);

		return result;
	}
}