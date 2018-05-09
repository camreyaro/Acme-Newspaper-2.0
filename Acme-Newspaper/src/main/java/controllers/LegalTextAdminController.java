
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.LegalTextService;
import domain.LegalText;

@Controller()
@RequestMapping("/legaltext")
public class LegalTextAdminController extends AbstractController {

	@Autowired
	private LegalTextService	legalTextService;


	@RequestMapping(value = "/administrator/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int legalTextId) {
		ModelAndView result;
		LegalText legalText;

		legalText = this.legalTextService.findOne(legalTextId);
		result = this.createEditModelAndView(legalText);
		return result;
	}

	@RequestMapping(value = "/administrator/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid LegalText legalText, BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(legalText);
		else
			try {
				this.legalTextService.save(legalText);
				result = new ModelAndView("redirect:/legaltext/index.do");
			} catch (Throwable oops) {
				result = this.createEditModelAndView(legalText, "master.page.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(LegalText legalText) {
		return this.createEditModelAndView(legalText, null);
	}

	protected ModelAndView createEditModelAndView(LegalText legalText, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("legaltext/administrator/edit");
		result.addObject("legaltext", legalText);
		result.addObject("message", messageCode);

		return result;
	}

}
