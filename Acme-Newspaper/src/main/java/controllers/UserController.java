
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.ArticleService;
import services.ChirpService;
import services.SuscriptionService;
import services.UserService;
import domain.Actor;
import domain.Article;
import domain.Chirp;
import domain.User;
import forms.UserForm;

@Controller
@RequestMapping("/user")
public class UserController extends AbstractController {

	// Supporting services ----------------------------------------------------
	@Autowired
	UserService			userService;
	@Autowired
	ActorService		actorService;
	@Autowired
	ArticleService		articleService;
	@Autowired
	ChirpService		chirpService;
	@Autowired
	SuscriptionService	suscriptionService;


	// Display ----------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam(required = false) Integer userId) {
		ModelAndView result = null;
		Actor actor = null;
		Collection<Article> articles = new ArrayList<>();
		Collection<Chirp> chirps = new ArrayList<>();
		Collection<Article> privateArticlesSuscribed = new ArrayList<>();
		Collection<Article> privateArticlesNotSuscribed = new ArrayList<>();
		Collection<Article> AllPrivateArticles = new ArrayList<>();
		Boolean owner = false;

		//Si la userID es nula y no está logueado, lo envío a la lista de usuarios.
		if (userId == null)
			try {
				userId = this.actorService.findByPrincipal().getId();
				actor = this.actorService.findOne(userId);
				articles = this.articleService.getPublishedArticlesByUserId(userId);
				chirps = this.chirpService.findChirpsByUserId(userId);
				result = new ModelAndView("user/display");
				if (LoginService.getPrincipal().isAuthority("USER")) {
					result.addObject("numFollowers", this.userService.getFollowers(userId).size());
					owner = true;
					result.addObject("owner", owner);
				}
			} catch (Throwable oops) {
				result = this.list(null);
				//				result.addObject("users", this.userService.findAll());
				return result;
			}

		//Si la userId no es de ningún actor, devuelve a la lista de usuarios. 
		//Puede darse el caso de que un no-autenticado intente meter la url con un userId no válido.
		else {
			try {

				actor = this.actorService.findOne(userId);

				if (actor == null) {
					result = new ModelAndView("user/list");
					result.addObject("users", this.userService.findAll());
					return result;
				}

				if (LoginService.getPrincipal().getId() == actor.getUserAccount().getId())
					owner = true;
			} catch (Throwable oops) {
			}

			articles = this.articleService.getPublishedArticlesByUserId(userId);
			chirps = this.chirpService.findChirpsByUserId(userId);
			result = new ModelAndView("user/display");
			result.addObject("numFollowers", this.userService.getFollowers(userId).size());

			try {
				if (LoginService.getPrincipal().isAuthority("CUSTOMER")) {
					privateArticlesSuscribed = this.articleService.getPrivatePublishedSuscribedArticlesByUserId(userId);
					privateArticlesNotSuscribed = this.articleService.getPrivatePublishedNotSuscribedArticlesByUserId(userId);
					result.addObject("privateArticlesSuscribed", privateArticlesSuscribed);
					result.addObject("privateArticlesNotSuscribed", privateArticlesNotSuscribed);

				} else if (LoginService.getPrincipal().isAuthority("USER") || LoginService.getPrincipal().isAuthority("ADMIN")) {
					AllPrivateArticles = this.articleService.getAllPrivatePublishedArticlesByUserId(userId);
					result.addObject("AllPrivateArticles", AllPrivateArticles);

					User me = (User) this.actorService.findByPrincipal();
					User following = (User) actor;
					result.addObject("owner", owner);
					result.addObject("isFollower", me.getFollowing().contains(following));
				}
			} catch (Throwable oops) {
			}
		}

		result.addObject("user", actor);
		result.addObject("articles", articles);
		result.addObject("chirps", chirps);

		return result;
	}

	// Listing ----------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) Integer pageNumber) {
		ModelAndView result;
		Page<User> pageObject;

		if (pageNumber == null)
			pageNumber = 1;

		pageObject = this.userService.findAllPaginate(pageNumber, 3);

		result = new ModelAndView("user/list");
		result.addObject("users", pageObject.getContent());
		result.addObject("totalPages", pageObject.getTotalPages());
		result.addObject("pageNumber", pageNumber);
		result.addObject("pageSize", 3);

		return result;
	}

	@RequestMapping(value = "/followers", method = RequestMethod.GET)
	public ModelAndView followers() {
		ModelAndView result;
		Collection<User> users = new ArrayList<>();

		User user = (User) this.actorService.findByPrincipal();
		users.addAll(this.userService.getFollowers(user.getId()));
		result = new ModelAndView("user/list");
		result.addObject("users", users);

		return result;
	}

	@RequestMapping(value = "/following", method = RequestMethod.GET)
	public ModelAndView following() {
		ModelAndView result;
		Collection<User> users = new ArrayList<>();

		User user = (User) this.actorService.findByPrincipal();
		users.addAll(user.getFollowing());
		result = new ModelAndView("user/list");
		result.addObject("users", users);

		return result;
	}

	@RequestMapping(value = "/follow", method = RequestMethod.GET)
	public ModelAndView follow(@RequestParam(required = true) Integer userId) {
		ModelAndView result;

		result = new ModelAndView("redirect:display.do?userId=" + userId);
		this.userService.follow(userId);

		return result;
	}

	@RequestMapping(value = "/unfollow", method = RequestMethod.GET)
	public ModelAndView unfollow(@RequestParam(required = true) Integer userId) {
		ModelAndView result;

		result = new ModelAndView("redirect:display.do?userId=" + userId);
		this.userService.unfollow(userId);

		return result;
	}
	// Registration -----------------------------------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView result;
		User user;

		user = this.userService.create();

		result = new ModelAndView("user/register");
		result.addObject("user", user);

		return result;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "register")
	public ModelAndView saveFromCreate(User user, BindingResult binding) {
		ModelAndView result;

		this.userService.reconstruct(user, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(user);
		else if (user.getHasConfirmedTerms() == false)
			result = this.createEditModelAndView(user, "user.mustAccept");
		else
			try {
				Collection<User> f = user.getFollowing();
				f.add(user);
				user.setFollowing(f);
				this.userService.saveFromCreate(user);
				result = new ModelAndView("redirect:/security/login.do");
			} catch (Throwable oops) {
				String errorMessage = "user.registration.error";

				if (oops.getMessage().contains("user.duplicated.username") || oops.getMessage().contains("message.error"))
					errorMessage = oops.getMessage();

				result = this.createEditModelAndView(user, errorMessage);
			}

		return result;
	}

	// EDITION -----------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		User user;
		UserForm form = new UserForm();

		user = (User) this.actorService.findOne(this.actorService.findByPrincipal().getId());
		form.setEmailAddress(user.getEmailAddress());
		form.setName(user.getName());
		form.setPhoneNumber(user.getPhoneNumber());
		form.setPostalAddress(user.getPostalAddress());
		form.setSurname(user.getSurname());
		form.setUsername(user.getUserAccount().getUsername());

		result = new ModelAndView("user/edit");
		result.addObject("userForm", form);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid UserForm form, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndViewForm(form);
		else
			try {

				User user2 = this.userService.reconstruct(form);
				this.userService.save(user2);
				result = new ModelAndView("redirect:display.do");
			} catch (Throwable oops) {
				String errorMessage = "user.registration.error";

				result = this.createEditModelAndViewEdit(form, errorMessage);
			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(User user) {
		ModelAndView result;

		result = this.createEditModelAndView(user, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(User user, String message) {
		ModelAndView result;

		result = new ModelAndView("user/register");
		result.addObject("user", user);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndViewEdit(UserForm user, String message) {
		ModelAndView result;

		result = new ModelAndView("user/edit");
		result.addObject("userForm", user);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndViewForm(UserForm formServicio) {
		ModelAndView result;

		result = this.createEditModelAndViewForm(formServicio, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewForm(UserForm form, String message) {
		ModelAndView result;

		result = new ModelAndView("user/edit");
		result.addObject("userForm", form);
		result.addObject("message", message);

		return result;
	}

}
