/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.LegalTextService;
import domain.LegalText;

@Controller
@RequestMapping("/legaltext")
public class LegalTextController extends AbstractController {

	//  Services 
	@Autowired
	private LegalTextService	legalTexService;


	// Constructors -----------------------------------------------------------

	public LegalTextController() {
		super();
	}

	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/index")
	public ModelAndView index(Locale locale) {
		ModelAndView result;
		LegalText termsAndConditions,cookies;
		
		termsAndConditions = this.legalTexService.getTermsAndConditions(locale);
		cookies = this.legalTexService.getCookies(locale);

		result = new ModelAndView("legaltext/index");
		result.addObject("termsAndConditions", termsAndConditions);
		result.addObject("cookies", cookies);

		return result;
	}
}
