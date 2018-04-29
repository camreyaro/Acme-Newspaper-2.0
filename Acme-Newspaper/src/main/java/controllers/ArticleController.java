/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import org.apache.avro.reflect.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
import domain.Actor;
import domain.Advertisement;
import domain.Article;
import domain.Newspaper;

@Controller
@RequestMapping("/newspaper/article")
public class ArticleController extends AbstractController {

	@Autowired
	ArticleService			articleService;

	@Autowired
	NewspaperService		newspaperService;

	@Autowired
	ActorService			actorService;

	@Autowired
	SuscriptionService		suscriptionService;

	@Autowired
	AdvertisementService	advertisementService;


	// Constructors -----------------------------------------------------------

	public ArticleController() {
		super();
	}

	// Action-1 ---------------------------------------------------------------		

	@RequestMapping("/administrator/spamArticlesList")
	public ModelAndView spamArticlesList() {
		ModelAndView result;
		Collection<Article> spamArticles;

		spamArticles = this.articleService.getArticlesWithSpamWords();

		result = new ModelAndView("administrator/spamArticlesList");
		result.addObject("spamArticles", spamArticles);

		return result;
	}

	// --- LIST AND DISPLAY ---
	@RequestMapping("/display")
	public ModelAndView display(@RequestParam String articleId) {
		ModelAndView res = new ModelAndView("welcome/index");
		Boolean createFU = false;
		Boolean canEdit = false;
		Boolean esAdmin = false;
		Boolean esMio = false;
		Boolean esCustomer = false;
		Boolean esPublico = false;

		Integer id = new Integer(articleId);
		Article a = this.articleService.findOne(id);
		String newspaperId = String.valueOf(a.getNewspaper().getId());

		Boolean seeFU = a.getSaved() && a.getNewspaper().getPublished();

		Collection<String> susURLs = this.articleService.getURLsDeUnArticleId(id);

		Advertisement advertisement;
		advertisement = this.advertisementService.findRandomAdvertisementByNewspaperId(a.getNewspaper().getId());

		if (a.getNewspaper().getPublicNp() && a.getNewspaper().getPublished())
			esPublico = true;

		try {
			Actor actor = this.actorService.findByPrincipal();

			if (a.getCreator().getId() == actor.getId())
				esMio = true;

			if (LoginService.getPrincipal().isAuthority("ADMIN"))
				esAdmin = true;

			if (LoginService.getPrincipal().isAuthority("CUSTOMER") && a.getNewspaper().getPublished() && this.suscriptionService.isCustomerSuscribe(newspaperId))
				esCustomer = true;

		} catch (Throwable oops) {
			if (esMio || esPublico || esAdmin || esCustomer) {
				res = new ModelAndView("newspaper/article/display");
				res.addObject("article", a);
				res.addObject("articleId", a.getId());
				res.addObject("createFU", createFU);
				res.addObject("canEdit", canEdit);
				res.addObject("seeFU", seeFU);
				res.addObject("susURLs", susURLs);
				res.addObject("advertisement", advertisement);
				return res;
			} else
				res = new ModelAndView("welcome/index");
		}
		if (esMio || esPublico || esAdmin || esCustomer) {
			res = new ModelAndView("newspaper/article/display");
			res.addObject("article", a);
			res.addObject("articleId", a.getId());
			res.addObject("createFU", createFU);
			res.addObject("canEdit", canEdit);
			res.addObject("seeFU", seeFU);
			res.addObject("susURLs", susURLs);
			res.addObject("advertisement", advertisement);
			return res;
		}
		return res;
	}
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam String newspaperId) {
		ModelAndView res = null;
		Integer id = new Integer(newspaperId);
		Newspaper n = this.newspaperService.findOne(id);

		if ((n.getPublished() && n.getPublicNp()) || (!n.getPublicNp() && LoginService.getPrincipal().isAuthority("USER"))) {
			res = new ModelAndView("newspaper/article/list");
			//Mirar si es privado y que el usuario sea customer suscrito
			res.addObject("articles", n.getArticles());
			res.addObject("newspaperId", n.getId());
			if (!n.getPublicNp() && LoginService.getPrincipal().isAuthority("CUSTOMER"))
				try {

					if (this.suscriptionService.isCustomerSuscribe(newspaperId)) {
						res.addObject("articles", n.getArticles());
						res.addObject("newspaperId", n.getId());
					} else
						return new ModelAndView("welcome/index");
				} catch (Throwable oops) {
					return new ModelAndView("welcome/index");
				}
		} else
			try {

				if (this.suscriptionService.isCustomerSuscribe(newspaperId)) {
					res.addObject("articles", n.getArticles());
					res.addObject("newspaperId", n.getId());
				}

				Actor actor = this.actorService.findByPrincipal();
				if (actor.getId() == n.getPublisher().getId() || !n.getPublicNp())
					res = new ModelAndView("newspaper/list");
			} catch (Throwable oops) {
				res = new ModelAndView("newspaper/article/list");
				res.addObject("articles", n.getArticles());
				res.addObject("newspaperId", n.getId());
			}

		return res;

	}

	@RequestMapping("/user/myList")
	public ModelAndView myList() {
		ModelAndView res;

		Actor a = this.actorService.findByPrincipal();
		Collection<Article> articles = this.articleService.articlesByUserId(a.getId());
		res = new ModelAndView("newspaper/article/user/myList");
		res.addObject("articles", articles);

		return res;

	}

	// --- SEARCHED LIST --- 
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView search() {
		ModelAndView result;
		result = new ModelAndView("newspaper/article/search");
		return result;
	}

	@RequestMapping(value = "/searchedList", method = RequestMethod.GET)
	public ModelAndView searchedList(@RequestParam(value = "keyword", required = false) @Nullable String keyword) {
		ModelAndView result;
		Collection<Article> articles;
		Boolean esAdmin = false;

		try {

			esAdmin = LoginService.getPrincipal().isAuthority("ADMIN");
		} catch (Throwable oops) {
		}

		if (keyword == null || keyword == "" || keyword.length() < 2)
			articles = this.articleService.findAllValidAndPublic();
		else {

			if (esAdmin)
				articles = this.articleService.findAdminByKeyword(keyword);

			try {
				this.suscriptionService.isCustomerSuscribe("00");
				Actor a = this.actorService.findByPrincipal();
				if (LoginService.getPrincipal().isAuthority("CUSTOMER"))
					articles = this.articleService.findSuscriptedArticlesByKeyword(keyword, a.getId());
				else
					articles = this.articleService.findPublicArticlesByKeyword(keyword);

			} catch (Throwable oops) {

				articles = this.articleService.findPublicArticlesByKeyword(keyword);
			}
		}

		result = new ModelAndView("newspaper/article/searchedList");
		result.addObject("articles", articles);
		result.addObject("requestURI", "newspaper/article/searchedList.do");
		return result;
	}

	// --- CREATE AND SAVE.FOR.CREATE ---

	@RequestMapping("/user/create")
	public ModelAndView create(@RequestParam String newspaperId) {
		ModelAndView res;
		Integer id = new Integer(newspaperId);
		Newspaper n = this.newspaperService.findOne(id);

		if (n.getPublished())
			res = new ModelAndView("welcome/index");
		else {
			Article a = this.articleService.create(n);
			res = new ModelAndView("newspaper/article/user/create");
			res.addObject("article", a);
			res.addObject("newspaperId", n.getId());
		}

		return res;
	}

	@RequestMapping(value = "/user/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Article article, BindingResult binding) {
		ModelAndView res;
		Article art = this.articleService.reconstruct(article, binding);

		if (binding.hasErrors())
			res = this.createCreateModelAndView(art);
		else
			try {
				Article toSave = art;

				this.articleService.save(toSave);

				Newspaper newsp = toSave.getNewspaper();

				res = new ModelAndView("newspaper/article/list");
				res.addObject("articles", newsp.getArticles());
				res.addObject("newspaperId", newsp.getId());
				//res.addObject("articled", rend.getId());
			} catch (Throwable oops) {
				String messageCode = "article.commit.error";
				if (oops.getMessage().contains("org.hibernate.validator.constraints.URL.message"))
					messageCode = "org.hibernate.validator.constraints.URL.message";
				res = this.createCreateModelAndView(art, messageCode);

			}
		return res;
	}

	// --- EDIT AND SAVE.FOR.EDIT ---

	@RequestMapping("/user/edit")
	public ModelAndView edit(@RequestParam String articleId) {
		ModelAndView res;
		Integer id = new Integer(articleId);
		Article a = this.articleService.findOne(id);

		if (a.getNewspaper().getPublished() || a.getSaved())
			res = new ModelAndView("welcome/index");
		else {
			res = new ModelAndView("newspaper/article/user/edit");

			res.addObject("article", a);
			res.addObject("newspaperId", a.getNewspaper().getId());
		}

		return res;
	}

	@RequestMapping(value = "/user/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveOfEdit(Article article, BindingResult binding) {
		ModelAndView res;
		Article art = this.articleService.reconstruct(article, binding);

		if (binding.hasErrors())
			res = this.createEditModelAndView(art);
		else
			try {

				Article toSave = art;

				this.articleService.save(toSave);

				Newspaper newsp = toSave.getNewspaper();

				res = new ModelAndView("newspaper/article/list");
				res.addObject("articles", newsp.getArticles());
				res.addObject("newspaperId", newsp.getId());
				//res.addObject("articled", rend.getId());
			} catch (Throwable oops) {
				res = this.createEditModelAndView(art, "article.commit.error");

			}
		return res;
	}

	@RequestMapping("/administrator/delete")
	public ModelAndView deleteNewspaper(@RequestParam(value = "articleId", required = true) int articleId) {
		ModelAndView result;
		Article article;

		article = this.articleService.findOne(articleId);

		try {
			this.articleService.delete(article);
		} catch (Throwable o) {
			return new ModelAndView("redirect:/newspaper/article/display.do?articleId=" + articleId);
		}

		result = new ModelAndView("redirect:/newspaper/display.do?newspaperId=" + article.getNewspaper().getId());

		return result;
	}

	// --- ANCILLARY METHODS ---
	protected ModelAndView createCreateModelAndView(Article a) {

		return this.createCreateModelAndView(a, null);
	}

	protected ModelAndView createCreateModelAndView(Article a, String msg) {
		ModelAndView res;
		res = new ModelAndView("newspaper/article/user/create");

		res.addObject("article", a);
		res.addObject("newspaperId", a.getNewspaper().getId());
		res.addObject("message", msg);

		return res;
	}

	protected ModelAndView createEditModelAndView(Article a) {
		return this.createEditModelAndView(a, null);
	}

	protected ModelAndView createEditModelAndView(Article a, String msg) {
		ModelAndView res;
		res = new ModelAndView("newspaper/article/user/edit");

		res.addObject("article", a);
		res.addObject("newspaperId", a.getNewspaper().getId());
		res.addObject("message", msg);

		return res;
	}
}
