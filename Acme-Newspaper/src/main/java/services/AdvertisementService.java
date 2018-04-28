
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
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

	/*
	 * public Advertisement reconstruct(AdvertisementForm form) {
	 * 
	 * Advertisement result = (Advertisement) this.actorService.findByUserAccountUsername(form.getUserName());
	 * 
	 * result.setEmailAddress(form.getEmailAddress());
	 * result.setName(form.getName());
	 * result.setSurname(form.getSurname());
	 * result.setPhoneNumber(form.getPhoneNumber());
	 * result.setPostalAddress(form.getPostalAddress());
	 * 
	 * return result;
	 * 
	 * }
	 * 
	 * public Advertisement reconstruct(Advertisement advertisement, BindingResult binding) {
	 * 
	 * UserAccount userAccount;
	 * Collection<Authority> authorities;
	 * Authority auth;
	 * 
	 * advertisement.setId(0);
	 * advertisement.setVersion(0);
	 * 
	 * this.validator.validate(advertisement, binding);
	 * 
	 * return advertisement;
	 * 
	 * }
	 */

	//--------------Others
	public void saveAndFlush(Advertisement advertisement) {
		this.advertisementRepository.saveAndFlush(advertisement);
	}

	public Collection<Newspaper> findAdvertisedNewspapers(int agentId) {
		return this.findAdvertisedNewspapers(agentId);
	}

	public Collection<Newspaper> findNotAdvertisedNewspapers(int agentId) {
		return this.findNotAdvertisedNewspapers(agentId);
	}

	public Advertisement findRandomAdvertisementByNewspaperId(int newspaperId) {
		return this.findRandomAdvertisementByNewspaperId(newspaperId);
	}

}
