
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

import services.ActorService;
import services.NewspaperService;
import services.SuscriptionService;
import domain.Customer;
import domain.Newspaper;
import domain.Suscription;

@Controller
@RequestMapping("/suscription")
public class SuscriptionController extends AbstractController {

	// Supporting services ----------------------------------------------------
	@Autowired
	SuscriptionService	suscriptionService;
	@Autowired
	ActorService		actorService;
	@Autowired
	NewspaperService	newspaperService;


	// Suscription ----------------------------------------------------------------
	@RequestMapping(value = "/mySuscriptions", method = RequestMethod.GET)
	public ModelAndView display() {
		Collection<Suscription> suscriptions = new ArrayList<Suscription>();
		suscriptions = this.suscriptionService.suscriptionFromCustomer();

		ModelAndView result;
		Customer customer;

		customer = (Customer) this.actorService.findByPrincipal();

		result = new ModelAndView("suscription/mySuscriptions");
		result.addObject("suscription", suscriptions);

		return result;
	}

	// Suscription ----------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display2() {
		//TODO....

		ModelAndView result;
		Customer customer;

		customer = (Customer) this.actorService.findByPrincipal();

		result = new ModelAndView("suscription/display");
		result.addObject("customer", customer);

		return result;
	}
	// Suscript -----------------------------------------------------------

	@RequestMapping(value = "/suscribe")
	public ModelAndView register(@RequestParam final String newspaperId) {
		ModelAndView result;
		Suscription suscription;
		Newspaper newspaper;

		try { //Este try esta, por si ponen la id de algun newspaper que no exista
			newspaper = this.newspaperService.findOne(Integer.valueOf(newspaperId));
			suscription = this.suscriptionService.create();
			suscription.setNewspaper(newspaper);
			//Comprobamos que no este suscrito ni sea publico, aun asi, esta controlado el post
			if ((this.suscriptionService.isCustomerSuscribe(newspaperId) == true) || newspaper.getPublicNp() == true)
				result = new ModelAndView("redirect:/newspaper/list.do");
			else {
				result = new ModelAndView("suscription/suscribe");
				result.addObject("suscription", suscription);
				result.addObject("newspaperId", suscription.getNewspaper().getId());
			}
		} catch (final Throwable o) {
			result = new ModelAndView("redirect:/newspaper/list.do");
		}

		return result;
	}
	@RequestMapping(value = "/suscribe", method = RequestMethod.POST, params = "save")
	public ModelAndView saveSuscript(final Suscription sus, final BindingResult binding) {
		ModelAndView result;
		Suscription suscription;
		suscription = this.suscriptionService.reconstruct(sus, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(suscription);
		else
			try {
				this.suscriptionService.suscribe(suscription);
				//Esto tengo que cambiar el redirect, cuando sepa la url
				result = new ModelAndView("redirect:/newspaper/list.do");
			} catch (final Throwable oops) {
				String errorMessage = "suscription.suscript.error";
				if (oops.getMessage().contains("message.error"))
					errorMessage = oops.getMessage();

				result = this.createEditModelAndView(suscription, errorMessage);
			}

		return result;
	}
	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Suscription suscription) {
		ModelAndView result;

		result = this.createEditModelAndView(suscription, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Suscription suscription, final String message) {
		ModelAndView result;

		result = new ModelAndView("suscription/suscribe");
		result.addObject("suscription", suscription);
		result.addObject("newspaperId", suscription.getNewspaper().getId());
		result.addObject("message", message);

		return result;
	}

}
