
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.SuscriptionVolumenService;
import services.VolumenService;
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
}
