package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChirpService;

import domain.Article;
import domain.Chirp;
import domain.Newspaper;

@Controller()
@RequestMapping("/chirp/administrator")
public class ChirpAdminController {
	
	@Autowired
	private ChirpService	chirpService;
	
	@RequestMapping("/delete")
	public ModelAndView deleteChirp(@RequestParam(value = "chirpId", required = true) int chirpId) {
		ModelAndView result;
		Chirp chirp;

		chirp = chirpService.findOne(chirpId);
		
		try{
			chirpService.delete(chirp);
		}catch (Throwable o) { 
			return new ModelAndView("administrator/spamChirpsList");
		}

		result = new ModelAndView("administrator/spamChirpsList");

		return result;
	}
	
	@RequestMapping("/spamChirpsList")
	public ModelAndView spamNewspapersList() {
		ModelAndView result;
		Collection<Chirp> chirps;

		chirps = this.chirpService.getChirpsWithSpamWords();

		result = new ModelAndView("administrator/spamChirpsList");
		result.addObject("chirps", chirps);

		return result;
	}
	
	protected ModelAndView createEditModelAndView(Chirp chirp) {
		return this.createEditModelAndView(chirp, null);
	}

	protected ModelAndView createEditModelAndView(Chirp chirp, String messageCode) {
		ModelAndView result;

		result = new ModelAndView("chirp/user/edit");
		result.addObject("chirp", chirp);
		result.addObject("message", messageCode);

		return result;
	}

}
