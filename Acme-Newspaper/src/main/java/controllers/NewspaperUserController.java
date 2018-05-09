
package controllers;

import java.util.Collection;

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
import services.NewspaperService;
import domain.Newspaper;
import domain.User;

@Controller()
@RequestMapping("/newspaper/user")
public class NewspaperUserController extends AbstractController {

	@Autowired
	private NewspaperService	newspaperService;

	@Autowired
	private ActorService		actorService;


	// Listing ----------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam(required = false) Integer pageNumber) {
		ModelAndView result;
		Collection<Newspaper> newspapers;
		Page<Newspaper> pageObject;

		if (pageNumber == null)
			pageNumber = 1;

		User user = (User) this.actorService.findByPrincipal();

		pageObject = this.newspaperService.findAllByUserPaginate(pageNumber, 3, user.getId());
		newspapers = pageObject.getContent();

		result = new ModelAndView("newspaper/list");
		result.addObject("newspapers", newspapers);
		result.addObject("requestURI", "newspaper/user/list.do");
		result.addObject("actor", user);
		result.addObject("pageNumber", pageNumber);
		result.addObject("pageSize", 3);
		result.addObject("myList", true);
		result.addObject("totalPages", pageObject.getTotalPages());
		return result;
	}

	@RequestMapping(value = "/avaibleList", method = RequestMethod.GET)
	public ModelAndView avaibleList() {
		ModelAndView result;
		Collection<Newspaper> newspapers;

		newspapers = this.newspaperService.findAllAvaibles();

		result = new ModelAndView("newspaper/user/avaibleList");
		result.addObject("newspapers", newspapers);
		result.addObject("requestURI", "newspaper/user/avaibleList.do");
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Newspaper newspaper;

		newspaper = this.newspaperService.create();

		result = new ModelAndView("newspaper/user/edit");
		result.addObject("newspaper", newspaper);

		return result;
	}

	@RequestMapping("/publish")
	public ModelAndView publish(@RequestParam(value = "newspaperId", required = true) String newspaperId) {
		ModelAndView result;
		Newspaper newspaper;
		Integer id = Integer.valueOf(newspaperId);

		newspaper = this.newspaperService.findOne(id);

		try {
			this.newspaperService.publish(newspaper);
			result = new ModelAndView("redirect:list.do");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(newspaper, this.getCommitOrJavaError(oops.getMessage()));
		}

		return result;
	}

	@RequestMapping("/edit")
	public ModelAndView edit(@RequestParam(value = "newspaperId", required = true) String newspaperId) {
		ModelAndView result;
		Newspaper newspaper;

		Integer id = Integer.valueOf(newspaperId);
		newspaper = this.newspaperService.findOne(id);

		if (LoginService.getPrincipal().equals(newspaper.getPublisher().getUserAccount())) { //if owner
			result = new ModelAndView("newspaper/user/edit");
			result.addObject("newspaper", newspaper);
		} else
			result = new ModelAndView("newspaper/list");
		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Newspaper newspaper, BindingResult binding) {
		ModelAndView result;
		try {
			newspaper = this.newspaperService.reconstruct(newspaper, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(newspaper);
			else {
				this.newspaperService.save(newspaper);
				result = new ModelAndView("redirect:list.do");
			}
		} catch (Throwable oops) {
			result = this.createEditModelAndView(newspaper, this.getCommitOrJavaError(oops.getMessage()));
		}
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
