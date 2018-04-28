
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AgentService;
import domain.Agent;
import forms.AgentForm;

@Controller
@RequestMapping("/agent")
public class AgentController extends AbstractController {

	// Supporting services ----------------------------------------------------
	@Autowired
	AgentService	agentService;
	@Autowired
	ActorService	actorService;


	// Display ----------------------------------------------------------------
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display() {
		ModelAndView result;
		Agent agent;

		agent = (Agent) this.actorService.findByPrincipal();

		result = new ModelAndView("agent/display");
		result.addObject("agent", agent);

		return result;
	}

	// Registration -----------------------------------------------------------

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView result;
		Agent agent;

		agent = this.agentService.create();

		result = new ModelAndView("agent/register");
		result.addObject("agent", agent);

		return result;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "register")
	public ModelAndView saveFromCreate(Agent agent, BindingResult binding) {
		ModelAndView result;

		this.agentService.reconstruct(agent, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(agent);
		else if (agent.getHasConfirmedTerms() == false)
			result = this.createEditModelAndView(agent, "agent.mustAccept");
		else
			try {
				this.agentService.saveFromCreate(agent);
				result = new ModelAndView("redirect:/security/login.do");
			} catch (Throwable oops) {
				String errorMessage = "agent.registration.error";

				if (oops.getMessage().contains("agent.duplicated.username") || oops.getMessage().contains("message.error"))
					errorMessage = oops.getMessage();

				result = this.createEditModelAndView(agent, errorMessage);
			}

		return result;
	}

	// EDITION -----------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		Agent agent;
		AgentForm form = new AgentForm();

		agent = (Agent) this.actorService.findByPrincipal();
		form.setEmailAddress(agent.getEmailAddress());
		form.setName(agent.getName());
		form.setPhoneNumber(agent.getPhoneNumber());
		form.setPostalAddress(agent.getPostalAddress());
		form.setSurname(agent.getSurname());
		form.setUserName(agent.getUserAccount().getUsername());

		result = new ModelAndView("agent/edit");
		result.addObject("agentForm", form);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid AgentForm form, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndViewForm(form);
		else
			try {
				Agent agent = this.agentService.reconstruct(form);
				this.agentService.save(agent);
				result = new ModelAndView("redirect:display.do");
			} catch (Throwable oops) {
				String errorMessage = oops.getMessage();

				result = this.createEditModelAndViewForm(form, errorMessage);
			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(Agent agent) {
		ModelAndView result;

		result = this.createEditModelAndView(agent, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(Agent agent, String message) {
		ModelAndView result;

		result = new ModelAndView("agent/register");
		result.addObject("agent", agent);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndViewForm(AgentForm agentForm) {
		ModelAndView result;

		result = this.createEditModelAndViewForm(agentForm, null);

		return result;
	}

	protected ModelAndView createEditModelAndViewForm(AgentForm agentForm, String message) {
		ModelAndView result;

		result = new ModelAndView("agent/edit");
		result.addObject("agentForm", agentForm);
		result.addObject("message", message);

		return result;
	}

}
