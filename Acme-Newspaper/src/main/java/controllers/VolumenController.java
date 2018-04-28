
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.VolumenService;
import domain.Actor;
import domain.Newspaper;
import domain.Volumen;

@Controller
@RequestMapping("/volumen")
public class VolumenController extends AbstractController {

	@Autowired
	private VolumenService	volumenService;
	@Autowired
	private ActorService	actorService;


	@RequestMapping("/list")
	public ModelAndView list() {
		final ModelAndView res;
		final Collection<Volumen> volumens = this.volumenService.findAll();

		res = new ModelAndView("volumen/list");
		res.addObject("requestURI", "volumen/list.do");
		res.addObject("volumens", volumens);

		return res;

	}

	@RequestMapping("newspaper/list")
	public ModelAndView newspaperList(@RequestParam final Integer volumenId) {
		final ModelAndView res;
		Boolean creator = false;
		final Volumen volumen = this.volumenService.findOne(volumenId);
		final Collection<Newspaper> newspapers = this.volumenService.getAllNewspaper(volumenId);

		try {
			final Actor actor = this.actorService.findByPrincipal();

			if (actor.equals(volumen.getUser()))
				creator = true;

		} catch (final Throwable oops) {
			creator = false;
		}

		res = new ModelAndView("newspaper/list");

		res.addObject("newspapers", newspapers);
		res.addObject("requestURI", "volumen/newspaper/list.do");
		res.addObject("creator", creator);
		res.addObject("volumen", volumen);

		return res;

	}
}
