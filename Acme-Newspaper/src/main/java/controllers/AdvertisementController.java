
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.LoginService;
import services.ActorService;
import services.AdvertisementService;
import services.NewspaperService;
import domain.Advertisement;
import domain.Agent;
import domain.Newspaper;

@Controller()
@RequestMapping("/")
public class AdvertisementController extends AbstractController {

	@Autowired
	private NewspaperService		newspaperService;

	@Autowired
	private AdvertisementService	advertisementService;

	@Autowired
	private ActorService			actorService;


	// Listing ----------------------------------------------------------------
	@RequestMapping(value = "advertisement/agent/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Advertisement> advertisements;
		Agent agent = (Agent) this.actorService.findByPrincipal();

		advertisements = this.advertisementService.findAdvertisementsByAgent(agent.getId());

		result = new ModelAndView("advertisement/agent/list");
		result.addObject("advertisements", advertisements);
		result.addObject("requestURI", "advertisement/agent/list.do");
		return result;
	}

	@RequestMapping(value = "newspaper/advertisement/agent/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam(value = "newspaperId", required = true) String newspaperId) {
		ModelAndView result;
		Newspaper newspaper;
		Advertisement advertisement;

		try {
			Integer id = Integer.parseInt(newspaperId);
			newspaper = this.newspaperService.findOne(id);
			if (!newspaper.getPublished())
				throw new Throwable();
			advertisement = this.advertisementService.create(newspaper);
		} catch (Throwable oops) {
			return new ModelAndView("redirect:list.do");
		}
		result = new ModelAndView("advertisement/agent/edit");
		result.addObject("advertisement", advertisement);

		return result;
	}

	@RequestMapping(value = "advertisement/agent/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam(value = "advertisementId", required = true) String advertisementId) {
		ModelAndView result;
		Advertisement advertisement;

		try {
			Integer id = Integer.parseInt(advertisementId);
			advertisement = this.advertisementService.findOne(id);
			if (!advertisement.getAgent().getUserAccount().equals(LoginService.getPrincipal()))
				throw new Throwable();
		} catch (Throwable oops) {
			return new ModelAndView("redirect:list.do");
		}
		result = new ModelAndView("advertisement/agent/edit");
		result.addObject("advertisement", advertisement);

		return result;
	}

	@RequestMapping(value = "advertisement/agent/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Advertisement advertisement, BindingResult binding) {
		ModelAndView result;

		try {
			advertisement = this.advertisementService.reconstruct(advertisement, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(advertisement);
			else {
				this.advertisementService.save(advertisement);
				result = new ModelAndView("redirect:list.do");
			}
		} catch (final Throwable oops) {
			System.out.println(oops);
			result = this.createEditModelAndView(advertisement, this.getCommitOrJavaError(oops.getMessage()));
		}
		return result;
	}

	//Administrator ---------------------

	@RequestMapping("advertisement/administrator/spamAdvertisementsList")
	public ModelAndView spamNewspapersList(@RequestParam(value = "viewAll", required = false) String viewAll) {
		ModelAndView result;
		Collection<Advertisement> spamAds;

		if (viewAll == null)
			spamAds = this.advertisementService.getAdvertisementWithSpamWords();
		else
			spamAds = this.advertisementService.findAll();

		result = new ModelAndView("administrator/spamAdvertisementsList");
		result.addObject("spamAds", spamAds);

		return result;
	}

	@RequestMapping("advertisement/administrator/delete")
	public ModelAndView deleteNewspaper(@RequestParam(value = "advertisementId", required = true) int advertisementId) {
		ModelAndView result;
		Advertisement advertisement;

		try {
			advertisement = this.advertisementService.findOne(advertisementId);
			this.advertisementService.delete(advertisement);
		} catch (Throwable o) {
			result = new ModelAndView("redirect:spamAdvertisementsList.do");
		}

		result = new ModelAndView("redirect:spamAdvertisementsList.do");

		return result;
	}

	//Others   ----------------------

	protected ModelAndView createEditModelAndView(Advertisement advertisement) {
		return this.createEditModelAndView(advertisement, null);
	}

	protected ModelAndView createEditModelAndView(Advertisement advertisement, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("advertisement/agent/edit");
		result.addObject("advertisement", advertisement);
		result.addObject("message", messageCode);

		return result;
	}

}
