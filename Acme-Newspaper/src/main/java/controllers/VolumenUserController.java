
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.VolumenService;
import domain.Volumen;

@Controller
@RequestMapping("/volumen/user")
public class VolumenUserController extends AbstractController {

	@Autowired
	private VolumenService	volumenService;


	@RequestMapping("/myList")
	public ModelAndView myList() {
		final ModelAndView res;
		final Collection<Volumen> volumens = this.volumenService.getMyCreatedVolumens();

		res = new ModelAndView("volumen/list");

		res.addObject("volumens", volumens);
		res.addObject("requestURI", "volumen/user/myList.do");

		return res;
	}

	@RequestMapping("/edit")
	public ModelAndView edit() {
		final ModelAndView res;
		final Volumen v = this.volumenService.create();

		res = new ModelAndView("volumen/edit");
		res.addObject("volumen", v);

		return res;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Volumen volumen, final BindingResult binding) {
		ModelAndView res;

		this.volumenService.reconstruct(volumen, binding);
		if (binding.hasErrors())
			res = this.createEditModelAndView(volumen);
		else
			try {
				this.volumenService.save(volumen);
				res = new ModelAndView("redirect:myList.do");
			} catch (final Throwable oops) {
				String msg = oops.getMessage();
				if (!msg.equals("volumen.edit.error") && !msg.equals("volumen.creator.error"))
					msg = "error.commit";
				res = this.createEditModelAndView(volumen, msg);
			}

		return res;
	}
	protected ModelAndView createEditModelAndView(final Volumen volumen) {
		return this.createEditModelAndView(volumen, null);
	}

	protected ModelAndView createEditModelAndView(final Volumen volumen, final String msg) {
		final ModelAndView res;

		res = new ModelAndView("volumen/edit");
		res.addObject("volumen", volumen);
		res.addObject("message", msg);

		return res;
	}

}
