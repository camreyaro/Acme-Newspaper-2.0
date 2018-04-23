/*
 * AdministratorController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ArticleService;
import services.ChirpService;
import services.FollowUpService;
import services.NewspaperService;
import services.UserService;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	ArticleService articleService;
	
	@Autowired
	NewspaperService newspaperService;
	@Autowired
	FollowUpService followUpService;
	@Autowired
	ChirpService chirpService;

	// Constructors -----------------------------------------------------------

	public AdministratorController() {
		super();
	}

	// Dashboard ----------------------------------------------------------------
		@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
		public ModelAndView list() {
			ModelAndView result;
			
			result = new ModelAndView("administrator/dashboard");
			result.addObject("avgNewspapersByWriter",userService.avgNewspapersByWriter());
			result.addObject("stddevNewspapersByWriter",userService.stddevNewspapersByWriter());
			result.addObject("avgArticlesByWriter",userService.avgArticlesByWriter());
			result.addObject("stddevArticlesByWriter",userService.stddevArticlesByWriter());
			result.addObject("avgArticlesByNewspaper",newspaperService.avgArticlesByNewspaper());
			result.addObject("stddevArticlesByNewspaper",newspaperService.stddevArticlesByNewspaper());
			result.addObject("newspapersUpper10PerCentArticles",newspaperService.getNewspapersUpper10PerCentArticles());
			result.addObject("newspapersLower10PerCentArticles",newspaperService.getNewspapersLower10PerCentArticles());
			result.addObject("ratioNewspapersCreated",userService.ratioNewspapersCreated());
			result.addObject("ratioArticlesCreated",userService.ratioArticlesCreated());
			result.addObject("avgFollowUpsPerArticle",articleService.avgFollowUpsPerArticle());
			result.addObject("avgChirpsPerUser",chirpService.avgChirpsPerUser());
			
			//B
			result.addObject("followUpsPerArticleUpOneWeek",followUpService.getFollowUpsPerArticleUpOneWeek());
			result.addObject("followUpsPerArticleUpTwoWeek",followUpService.getFollowUpsPerArticleUpTwoWeek());
			result.addObject("stddevChirpsPerUser",chirpService.stddevChirpsPerUser());
			result.addObject("ratioUserChirpsUpper75Avg",chirpService.ratioUserChirpsUpper75Avg());

			
			// A
			result.addObject("ratioPublicVsPrivateNewspapers",newspaperService.ratioPublicVsPrivateNewspapers());
			result.addObject("avgArticlesPerPrivateNewspapers",newspaperService.avgArticlesPerPrivateNewspapers());
			result.addObject("avgArticlesPerPublicNewspapers",newspaperService.avgArticlesPerPublicNewspapers());
			result.addObject("ratioSuscribersPerPrivateNewspapersVsCustomers",newspaperService.ratioSuscribersPerPrivateNewspapersVsCustomers());
			result.addObject("avgRatioPublicVsPrivateNewspapers",newspaperService.avgRatioPublicVsPrivateNewspapers());

			return result;
		}

	// Action-2 ---------------------------------------------------------------

	@RequestMapping("/action-2")
	public ModelAndView action2() {
		ModelAndView result;

		result = new ModelAndView("administrator/action-2");

		return result;
	}

}
