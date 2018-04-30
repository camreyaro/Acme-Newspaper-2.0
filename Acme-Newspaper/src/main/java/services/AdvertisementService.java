
package services;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AdvertisementRepository;
import security.LoginService;
import domain.Advertisement;
import domain.Agent;
import domain.CreditCard;
import domain.Newspaper;

@Service
@Transactional
public class AdvertisementService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private AdvertisementRepository	advertisementRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private Validator				validator;


	// Supporting services ----------------------------------------------------

	// Simple CRUD methods ----------------------------------------------------

	public Advertisement create(Newspaper newspaper) {
		Advertisement advertisement;
		Agent agent = (Agent) this.actorService.findByPrincipal();
		Assert.notNull(newspaper, "error.commit.null");

		advertisement = new Advertisement();
		advertisement.setAgent(agent);
		advertisement.setNewspaper(newspaper);
		advertisement.setCreditCard(new CreditCard());

		return advertisement;
	}

	/*
	 * public void saveFromCreate(Advertisement advertisement) {
	 * Assert.isTrue(this.actorService.findByUserAccountUsername(advertisement.getUserAccount().getUsername()) == null, "advertisement.duplicated.username");
	 * 
	 * Md5PasswordEncoder encoder;
	 * String hash;
	 * 
	 * encoder = new Md5PasswordEncoder();
	 * hash = encoder.encodePassword(advertisement.getUserAccount().getPassword(), null);
	 * 
	 * advertisement.getUserAccount().setPassword(hash);
	 * 
	 * advertisement.setConfirmMoment(new Date(System.currentTimeMillis()));
	 * 
	 * this.advertisementRepository.save(advertisement);
	 * 
	 * }
	 */

	public void save(Advertisement advertisement) {
		Assert.isTrue(LoginService.getPrincipal().equals(advertisement.getAgent().getUserAccount()), "error.commit.owner");
		Assert.isTrue(advertisement.getNewspaper().getPublished(), "error.commit.published");
		Assert.isTrue(advertisement.getCreditCard().validCreditCardDate(), "message.error.creditcardMonth");
		this.advertisementRepository.save(advertisement);
	}
	public Advertisement findOne(int advertisementId) {
		Advertisement advertisement = this.advertisementRepository.findOne(advertisementId);
		Assert.notNull(advertisement);
		return advertisement;
	}

	public Collection<Advertisement> findAll() {
		Collection<Advertisement> advertisements = this.advertisementRepository.findAll();

		return advertisements;
	}

	public void delete(Advertisement advertisement) {
		Assert.notNull(advertisement, "error.commit.null");
		Assert.isTrue(LoginService.getPrincipal().isAuthority("ADMIN"), "error.commit.permission");
		this.advertisementRepository.delete(advertisement);
	}

	public Advertisement reconstruct(final Advertisement advertisement, final BindingResult binding) {
		Advertisement res;
		final Advertisement original = this.advertisementRepository.findOne(advertisement.getId());

		if (advertisement.getId() == 0)
			res = advertisement;
		else {
			res = advertisement;
			res.setAgent(original.getAgent());
			res.setNewspaper(original.getNewspaper());
		}

		this.validator.validate(res, binding);
		return res;
	}

	//--------------Others
	public void saveAndFlush(Advertisement advertisement) {
		this.advertisementRepository.saveAndFlush(advertisement);
	}

	public Collection<Advertisement> findAdvertisementsByAgent(int agentId) {
		return this.advertisementRepository.findAdvertisementsByAgent(agentId);
	}

	public Collection<Newspaper> findAdvertisedNewspapers(int agentId) {
		return this.advertisementRepository.findAdvertisedNewspapers(agentId);
	}

	public Collection<Newspaper> findNotAdvertisedNewspapers(int agentId) {
		return this.advertisementRepository.findNotAdvertisedNewspapers(agentId);
	}

	public Advertisement findRandomAdvertisementByNewspaperId(int newspaperId) {
		ArrayList<Advertisement> randAdvs = this.advertisementRepository.findRandomAdvertisementByNewspaperId(newspaperId);
		if (randAdvs.size() > 0)
			return randAdvs.get(0);
		else
			return null;
	}

}
