
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CustomerService;
import domain.Customer;
import forms.CustomerForm;

@Controller
@RequestMapping("/customer")
public class CustomerController extends AbstractController {

	// Supporting services ----------------------------------------------------
	@Autowired
	CustomerService	customerService;
	@Autowired
	ActorService	actorService;


	// Display ----------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		Customer customer;

		customer = (Customer) this.actorService.findByPrincipal();

		result = new ModelAndView("customer/display");
		result.addObject("customer", customer);

		return result;
	}

	// Registration -----------------------------------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView result;
		Customer customer;

		customer = this.customerService.create();

		result = new ModelAndView("customer/register");
		result.addObject("customer", customer);

		return result;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "register")
	public ModelAndView saveFromCreate(Customer customer, BindingResult binding) {
		ModelAndView result;

		this.customerService.reconstruct(customer, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(customer);
		else if (customer.getHasConfirmedTerms() == false)
			result = this.createEditModelAndView(customer, "customer.mustAccept");
		else
			try {
				this.customerService.saveFromCreate(customer);
				result = new ModelAndView("redirect:/security/login.do");
			} catch (Throwable oops) {
				String errorMessage = "customer.registration.error";

				if (oops.getMessage().contains("customer.duplicated.username") || oops.getMessage().contains("message.error"))
					errorMessage = oops.getMessage();

				result = this.createEditModelAndView(customer, errorMessage);
			}

		return result;
	}

	// EDITION -----------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Customer customer;
		CustomerForm form = new CustomerForm();

		customer = (Customer) this.actorService.findByPrincipal();
		form.setEmailAddress(customer.getEmailAddress());
		form.setName(customer.getName());
		form.setPhoneNumber(customer.getPhoneNumber());
		form.setPostalAddress(customer.getPostalAddress());
		form.setSurname(customer.getSurname());
		form.setUserName(customer.getUserAccount().getUsername());

		result = new ModelAndView("customer/edit");
		result.addObject("customerForm", form);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid CustomerForm form, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndViewForm(form);
		else
			try {
				Customer customer = this.customerService.reconstruct(form);
				this.customerService.save(customer);
				result = new ModelAndView("redirect:display.do");
			} catch (Throwable oops) {
				String errorMessage = oops.getMessage();

				result = this.createEditModelAndViewForm(form, errorMessage);
			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(Customer customer) {
		ModelAndView result;

		result = this.createEditModelAndView(customer, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Customer customer, String message) {
		ModelAndView result;

		result = new ModelAndView("customer/register");
		result.addObject("customer", customer);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndViewForm(CustomerForm customerForm) {
		ModelAndView result;

		result = this.createEditModelAndViewForm(customerForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewForm(CustomerForm customerForm, String message) {
		ModelAndView result;

		result = new ModelAndView("customer/edit");
		result.addObject("customerForm", customerForm);
		result.addObject("message", message);

		return result;
	}

}
