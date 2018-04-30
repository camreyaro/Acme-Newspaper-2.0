
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
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
	public ModelAndView list(@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize) {
		final ModelAndView res;
		Collection<Volumen> volumens = new ArrayList<Volumen>();
		Page<Volumen> pageObject;
		
		if (pageNumber == null)
			pageNumber = 1;
		if (pageSize == null)
			pageSize = 5;

		try {
			if (LoginService.getPrincipal().isAuthority("CUSTOMER")){
				pageObject = this.volumenService.getMyNoSuscribedVolumensPaginate(pageNumber, pageSize);
				volumens = pageObject.getContent();
			
			}else{
				pageObject = this.volumenService.findAllPaginate(pageNumber, pageSize);
				volumens = pageObject.getContent();
			}

		} catch (final Throwable oops) {
			pageObject = this.volumenService.findAllPaginate(pageNumber, pageSize);
			volumens = pageObject.getContent();
		}

		res = new ModelAndView("volumen/list");
		res.addObject("requestURI", "volumen/list.do");
		res.addObject("volumens", volumens);
		res.addObject("pageNumber", pageNumber);
		res.addObject("pageSize", pageSize);
		res.addObject("totalPages", pageObject.getTotalPages());

		return res;

	}

	@RequestMapping("newspaper/list")
	public ModelAndView newspaperList(@RequestParam final Integer volumenId, 
			@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize) {
		final ModelAndView res;
		Boolean creator = false;
		final Volumen volumen = this.volumenService.findOne(volumenId);
		final Collection<Newspaper> newspapers = this.volumenService.getAllNewspaper(volumenId);
		Double totalPages = 0.;

		if (pageNumber == null)
			pageNumber = 1;
		if (pageSize == null)
			pageSize = 5;

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
		res.addObject("pageNumber", pageNumber);
		res.addObject("pageSize", pageSize);
		res.addObject("totalPages", totalPages);

		return res;

	}
}
