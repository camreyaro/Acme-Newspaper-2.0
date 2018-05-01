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

import services.AdvertisementService;
import services.ArticleService;
import services.ChirpService;
import services.FollowUpService;
import services.NewspaperService;
import services.SuscriptionVolumenService;
import services.UserService;
import services.VolumenService;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends AbstractController {

	@Autowired
	UserService							userService;

	@Autowired
	ArticleService						articleService;

	@Autowired
	NewspaperService					newspaperService;
	@Autowired
	FollowUpService						followUpService;
	@Autowired
	ChirpService						chirpService;
	@Autowired
	private VolumenService				volumenService;
	@Autowired
	private SuscriptionVolumenService	suscriptionVolumenService;
	@Autowired
	private AdvertisementService		advertisementService;


	// Constructors -----------------------------------------------------------

	public AdministratorController() {
		super();
	}

	// Dashboard ----------------------------------------------------------------
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		result = new ModelAndView("administrator/dashboard");
		result.addObject("avgNewspapersByWriter", this.userService.avgNewspapersByWriter());
		result.addObject("stddevNewspapersByWriter", this.userService.stddevNewspapersByWriter());
		result.addObject("avgArticlesByWriter", this.userService.avgArticlesByWriter());
		result.addObject("stddevArticlesByWriter", this.userService.stddevArticlesByWriter());
		result.addObject("avgArticlesByNewspaper", this.newspaperService.avgArticlesByNewspaper());
		result.addObject("stddevArticlesByNewspaper", this.newspaperService.stddevArticlesByNewspaper());
		result.addObject("newspapersUpper10PerCentArticles", this.newspaperService.getNewspapersUpper10PerCentArticles());
		result.addObject("newspapersLower10PerCentArticles", this.newspaperService.getNewspapersLower10PerCentArticles());
		result.addObject("ratioNewspapersCreated", this.userService.ratioNewspapersCreated());
		result.addObject("ratioArticlesCreated", this.userService.ratioArticlesCreated());
		result.addObject("avgFollowUpsPerArticle", this.articleService.avgFollowUpsPerArticle());
		result.addObject("avgChirpsPerUser", this.chirpService.avgChirpsPerUser());

		//B
		result.addObject("followUpsPerArticleUpOneWeek", this.followUpService.getFollowUpsPerArticleUpOneWeek());
		result.addObject("followUpsPerArticleUpTwoWeek", this.followUpService.getFollowUpsPerArticleUpTwoWeek());
		result.addObject("stddevChirpsPerUser", this.chirpService.stddevChirpsPerUser());
		result.addObject("ratioUserChirpsUpper75Avg", this.chirpService.ratioUserChirpsUpper75Avg());

		// A
		result.addObject("ratioPublicVsPrivateNewspapers", this.newspaperService.ratioPublicVsPrivateNewspapers());
		result.addObject("avgArticlesPerPrivateNewspapers", this.newspaperService.avgArticlesPerPrivateNewspapers());
		result.addObject("avgArticlesPerPublicNewspapers", this.newspaperService.avgArticlesPerPublicNewspapers());
		result.addObject("ratioSuscribersPerPrivateNewspapersVsCustomers", this.newspaperService.ratioSuscribersPerPrivateNewspapersVsCustomers());
		result.addObject("avgRatioPublicVsPrivateNewspapers", this.newspaperService.avgRatioPublicVsPrivateNewspapers());

		//2.0

		//C
		result.addObject("ratioNewspaperWithAdsVsWithoutAds", this.advertisementService.ratioNewspaperWithAdsVsWithoutAds());
		result.addObject("rationAdsWithSpamwords", this.advertisementService.rationAdsWithSpamwords());

		//B

		result.addObject("ratioSusVolVsSus", this.suscriptionVolumenService.ratioSV());
		result.addObject("avgNewsPerVol", this.volumenService.avgNewsPerVol());

		return result;
	}
}
