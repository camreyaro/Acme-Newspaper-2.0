
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
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
import domain.SpamWord;

@Service
@Transactional
public class AdvertisementService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private AdvertisementRepository	advertisementRepository;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private SpamWordService			spamWordService;

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

	public void save(Advertisement advertisement) {
		Assert.notNull(advertisement.getAgent(), "error.commit");
		Assert.isTrue(LoginService.getPrincipal().equals(advertisement.getAgent().getUserAccount()), "error.commit.owner");
		Assert.isTrue(advertisement.getNewspaper().getPublished(), "error.commit.published");
		Assert.isTrue(advertisement.getCreditCard().validCreditCardDate(), "message.error.creditcardMonth");
		this.advertisementRepository.save(advertisement);
	}
	public Advertisement findOne(int advertisementId) {
		Assert.isTrue(advertisementId > 0);
		Advertisement advertisement = this.advertisementRepository.findOne(advertisementId);
		Assert.notNull(advertisement, "error.commit.null");
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

	public Collection<Advertisement> getAdvertisementWithSpamWords() {

		final EntityManagerFactory factory = Persistence.createEntityManagerFactory("Acme-Newspaper");

		final EntityManager em = factory.createEntityManager();
		final FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
		em.getTransaction().begin();

		String regexp = "";
		for (final SpamWord sp : this.spamWordService.findAll())
			regexp += sp.getWord() + "|";

		final QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Advertisement.class).get();
		final org.apache.lucene.search.Query luceneQuery = qb.keyword().onFields("title", "urlBanner", "urlTargetPage").ignoreFieldBridge().matching(regexp).createQuery();

		final javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Advertisement.class);

		final List result = jpaQuery.getResultList();
		final Set<Advertisement> cc = new HashSet<>(result);

		em.getTransaction().commit();
		em.close();

		return cc;
	}

	// Reconstruct  -------------------------

	public Advertisement reconstruct(final Advertisement advertisement, final BindingResult binding) {
		Advertisement res;
		if (advertisement.getId() == 0)
			res = advertisement;
		else {
			Advertisement original = this.findOne(advertisement.getId());
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

	public Collection<Advertisement> getAdvertisementsByNewspaperId(int newspaperId) {
		return this.advertisementRepository.getAdvertisementsByNewspaperId(newspaperId);
	}

	//Dashboard
	public Double ratioNewspaperWithAdsVsWithoutAds() {
		return this.advertisementRepository.ratioNewspaperWithAdsVsWithoutAds();
	}

	public Double rationAdsWithSpamwords() {
		return 1.0 * (this.getAdvertisementWithSpamWords().size() / this.advertisementRepository.rationAdsWithSpamwords());
	}

}
