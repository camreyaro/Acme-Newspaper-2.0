package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SpamWordService;
import services.SuscriptionService;
import domain.Customer;
import domain.SpamWord;
import domain.Suscription;
import domain.User;
import forms.UserForm;

@Controller
@RequestMapping("/administrator")
public class TabooWordsController extends AbstractController {

	// Supporting services ----------------------------------------------------
	@Autowired
	SpamWordService spamWordService;

	// Listing ----------------------------------------------------------------
	@RequestMapping(value = "/listTabooWords", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<SpamWord> spamWords;

		spamWords = spamWordService.findAll();
		result = new ModelAndView("administrator/tabooWordsList");
		result.addObject("tabooWords", spamWords);

		return result;
	}

	// Edit ----------------------------------------------------------------
	@RequestMapping(value = "/createSpamWord", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		SpamWord spamWord;

		spamWord = spamWordService.create();
		result = new ModelAndView("administrator/editSpamWord");
		result.addObject("spamWord", spamWord);

		return result;
	}

	// Edit ----------------------------------------------------------------
	@RequestMapping(value = "/editSpamWord", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam int spamWordId) {
		ModelAndView result;
		SpamWord spamWord;

		spamWord = spamWordService.findOne(spamWordId);
		result = new ModelAndView("administrator/editSpamWord");
		result.addObject("spamWord", spamWord);

		return result;
	}

	@RequestMapping(value = "/editSpamWord", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid SpamWord spamWord,
			BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(spamWord);
		else
			try {
				this.spamWordService.save(spamWord);
				result = new ModelAndView("redirect:listTabooWords.do");
			} catch (Throwable oops) {
				String errorMessage = "user.registration.error";
				if (oops.getMessage().contains("spamword.word.exists")) {
					errorMessage = "spamword.word.exists";
				}
				result = this.createEditModelAndView(spamWord, errorMessage);
			}

		return result;
	}

	@RequestMapping(value = "/editSpamWord", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(SpamWord spamWord,
			BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(spamWord);
		else
			try {
				this.spamWordService.delete(spamWord);
				result = new ModelAndView("redirect:listTabooWords.do");
			} catch (Throwable oops) {
				String errorMessage = "user.registration.error";

				result = this.createEditModelAndView(spamWord, errorMessage);
			}

		return result;
	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(SpamWord spamWord) {
		ModelAndView result;

		result = this.createEditModelAndView(spamWord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(SpamWord spamWord,
			String message) {
		ModelAndView result;

		result = new ModelAndView("administrator/editSpamWord");
		result.addObject("spamWord", spamWord);
		result.addObject("message", message);

		return result;
	}

}
