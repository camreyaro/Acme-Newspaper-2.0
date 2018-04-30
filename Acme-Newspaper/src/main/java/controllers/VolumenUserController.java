
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

import services.ActorService;
import services.NewspaperService;
import services.VolumenService;
import domain.Actor;
import domain.Newspaper;
import domain.Volumen;
import forms.NewsToVolForm;

@Controller
@RequestMapping("/volumen/user")
public class VolumenUserController extends AbstractController {

	@Autowired
	private VolumenService		volumenService;
	@Autowired
	private NewspaperService	newspaperService;
	@Autowired
	private ActorService		actorService;


	@RequestMapping("/myList")
	public ModelAndView myList(@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize) {
		final ModelAndView res;
		Page<Volumen> pageObject;
		
		if (pageNumber == null)
			pageNumber = 1;
		if (pageSize == null)
			pageSize = 5;

		res = new ModelAndView("volumen/list");
		pageObject = this.volumenService.getMyNoSuscribedVolumensPaginate(pageNumber, pageSize);

		res.addObject("volumens", pageObject.getContent());
		res.addObject("pageNumber", pageNumber);
		res.addObject("pageSize", pageSize);
		res.addObject("totalPages", pageObject.getTotalPages());
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
	@RequestMapping("/add")
	public ModelAndView add(@RequestParam final Integer newspaperId) {
		final ModelAndView res;
		final NewsToVolForm form = new NewsToVolForm();
		final Collection<Volumen> volumens = this.volumenService.getMyAvailableVolumes(newspaperId);
		final Newspaper n = this.newspaperService.findOne(newspaperId);

		form.setNewspaper(n);

		res = new ModelAndView("volumen/add");
		res.addObject("newsToVolForm", form);
		res.addObject("volumens", volumens);

		return res;

	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final NewsToVolForm newsToVolForm, final BindingResult binding) {
		ModelAndView res;

		if (binding.hasErrors())
			res = this.createAdd(newsToVolForm);
		else
			try {
				this.volumenService.addNewspaper(newsToVolForm.getVolumen(), newsToVolForm.getNewspaper());
				res = new ModelAndView("redirect:myList.do");
			} catch (final Throwable oops) {
				String msg = oops.getMessage();

				if (!msg.equals("volumen.newspaper.published.error") && !msg.equals("volumen.creator.error") && !msg.equals("volumen.contained.newspaper.error"))
					msg = "error.commit";

				res = this.createAdd(newsToVolForm, msg);

			}

		return res;
	}

	@RequestMapping("/remove")
	public ModelAndView remove(@RequestParam final Integer newspaperId, @RequestParam final Integer volumenId) {
		ModelAndView res;
		final Volumen v = this.volumenService.findOne(volumenId);
		try {
			final Newspaper n = this.newspaperService.findOne(newspaperId);
			this.volumenService.removeNewspaper(v, n);
			res = this.createRemoveModelAndView(v);
		} catch (final Throwable oops) {
			res = this.createRemoveModelAndView(v);
			String msg = oops.getMessage();

			if (!msg.equals("volumen.uncontained.newspaper.error") && !msg.equals("volumen.creator.error"))
				msg = "error.commit";

			res.addObject("message", msg);
		}

		return res;

	}
	protected ModelAndView createRemoveModelAndView(final Volumen volumen) {
		final ModelAndView res;
		Boolean creator = false;
		final Collection<Newspaper> newspapers = this.volumenService.getAllNewspaper(volumen.getId());
		final Actor actor = this.actorService.findByPrincipal();

		if (actor.equals(volumen.getUser()))
			creator = true;

		res = new ModelAndView("newspaper/list");

		res.addObject("newspapers", newspapers);
		res.addObject("requestURI", "volumen/newspaper/list.do");
		res.addObject("creator", creator);
		res.addObject("volumen", volumen);

		return res;
	}
	protected ModelAndView createAdd(final NewsToVolForm newsToVolForm) {
		return this.createAdd(newsToVolForm, null);
	}
	protected ModelAndView createAdd(final NewsToVolForm newsToVolForm, final String msg) {
		final ModelAndView res;
		final Collection<Volumen> volumens = this.volumenService.getMyAvailableVolumes(newsToVolForm.getNewspaper().getId());

		res = new ModelAndView("volumen/add");
		res.addObject("newsToVolForm", newsToVolForm);
		res.addObject("volumens", volumens);
		res.addObject("message", msg);

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
