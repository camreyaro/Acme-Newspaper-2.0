
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ChirpService;
import domain.Chirp;
import domain.User;

@Controller()
@RequestMapping("/chirp/user")
public class ChirpUserController extends AbstractController {

	@Autowired
	private ChirpService	chirpService;

	@Autowired
	private ActorService	actorService;


	// Listing ----------------------------------------------------------------
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Chirp> chirps = new ArrayList<>();

		User user = (User) this.actorService.findByPrincipal();
		chirps.addAll(this.chirpService.findFollowingChirpsByUserId(user.getId()));

		result = new ModelAndView("chirp/user/list");
		result.addObject("chirps", chirps);
		result.addObject("requestURI", "chirp/user/list.do");
		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Chirp chirp;

		chirp = this.chirpService.create();

		result = new ModelAndView("chirp/user/edit");
		result.addObject("chirp", chirp);

		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Chirp chirp, BindingResult binding) {
		ModelAndView result;
		chirp.setId(0);
		chirp = this.chirpService.reconstruct(chirp, binding);
		if (binding.hasErrors()) {
			System.out.println(binding.getAllErrors());
			result = this.createEditModelAndView(chirp);
		} else
			try {
				this.chirpService.save(chirp);
				result = new ModelAndView("redirect:list.do");
			} catch (Throwable oops) {
				System.out.println("--------------------------: " + ExceptionUtils.getStackTrace(oops));
				result = this.createEditModelAndView(chirp, oops.getMessage());

			}
		return result;
	}

	protected ModelAndView createEditModelAndView(Chirp chirp) {
		return this.createEditModelAndView(chirp, null);
	}

	protected ModelAndView createEditModelAndView(Chirp chirp, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("chirp/user/edit");
		result.addObject("chirp", chirp);
		result.addObject("message", messageCode);

		return result;
	}
}
