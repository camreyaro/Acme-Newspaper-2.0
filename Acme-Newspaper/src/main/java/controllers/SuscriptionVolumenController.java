
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.SuscriptionVolumenService;
import services.VolumenService;
import domain.SuscriptionVolumen;
import domain.Volumen;

@Controller
@RequestMapping("/suscriptionVolumen")
public class SuscriptionVolumenController extends AbstractController {

	@Autowired
	private SuscriptionVolumenService	suscriptionVolumenService;
	@Autowired
	private VolumenService				volumenService;


	@RequestMapping("/myList")
	public ModelAndView myList() {
		final ModelAndView res;
		final Collection<Volumen> volumens = this.volumenService.getMyVolumens();

		res = new ModelAndView("volumen/list");

		res.addObject("requestURI", "suscriptionVolumen/myList.do");
		res.addObject("volumens", volumens);

		return res;
	}

	@RequestMapping("/create")
	public ModelAndView create(@RequestParam final Integer volumenId) {
		ModelAndView res;
		SuscriptionVolumen sv = new SuscriptionVolumen();
		final Volumen v = this.volumenService.findOne(volumenId);

		if (v == null)
			res = this.createListVolumenModelAndView();
		else {
			sv = this.suscriptionVolumenService.create(v);
			res = new ModelAndView("suscriptionVolumen/create");
		}
		res.addObject("suscriptionVolumen", sv);
		return res;

	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final SuscriptionVolumen suscriptionVolumen, final BindingResult binding) {
		ModelAndView res;
		this.suscriptionVolumenService.reconstruct(suscriptionVolumen, binding);
		if (binding.hasErrors())
			res = this.createCreateModelAndView(suscriptionVolumen);
		else
			try {
				this.suscriptionVolumenService.save(suscriptionVolumen);
				res = new ModelAndView("redirect:myList.do");
			} catch (final Throwable oops) {
				String msg = oops.getMessage();
				if (!msg.equals("suscriptionVolumen.alreadySubscribed.error") && !msg.equals("suscriptionVolumen.edit.error	") && !msg.equals("suscriptionVolumen.volumen.noExist.error") && !msg.equals("message.error.creditcard"))
					msg = "error.commit";
				res = this.createCreateModelAndView(suscriptionVolumen, msg);

			}

		return res;
	}

	protected ModelAndView createCreateModelAndView(final SuscriptionVolumen suscriptionVolumen) {
		return this.createCreateModelAndView(suscriptionVolumen, null);
	}

	protected ModelAndView createCreateModelAndView(final SuscriptionVolumen suscriptionVolumen, final String message) {
		ModelAndView res;

		final Volumen v = suscriptionVolumen.getVolumen();

		if (v == null)
			res = this.createListVolumenModelAndView();
		else {
			res = new ModelAndView("suscriptionVolumen/create");
			res.addObject("message", message);

		}
		res.addObject("suscriptionVolumen", suscriptionVolumen);

		return res;
	}

	protected ModelAndView createListVolumenModelAndView() {
		final ModelAndView res;
		Collection<Volumen> volumens = new ArrayList<Volumen>();

		volumens = this.volumenService.getMyNoSuscribedVolumens();

		res = new ModelAndView("volumen/list");
		res.addObject("requestURI", "volumen/list.do");
		res.addObject("volumens", volumens);
		res.addObject("message", "suscriptionVolumen.volumen.noExist.error");

		return res;
	}
}
