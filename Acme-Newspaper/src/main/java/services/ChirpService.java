
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ChirpRepository;
import security.LoginService;
import domain.Chirp;
import domain.SpamWord;
import domain.User;

@Service
@Transactional
public class ChirpService {

	// Managed repository ------------------------------ (Relacion con su propio repositorio)
	@Autowired
	private ChirpRepository	chirpRepository;

	@Autowired
	private ActorService	actorService;

	@Autowired
	Validator				validator;

	@Autowired
	private SpamWordService	spamWordService;


	// Simple CRUD methods ------------------------------ (Operaciones básicas, pueden tener restricciones según los requisitos)
	public Chirp create() {
		Chirp chirp = new Chirp();
		chirp.setUser((User) this.actorService.findByPrincipal());
		return chirp;
	}

	public Collection<Chirp> findAll() {
		Collection<Chirp> chirps;

		chirps = this.chirpRepository.findAll();
		Assert.notNull(chirps);

		return chirps;
	}

	public Chirp findOne(int chirpId) {
		Chirp chirp;
		chirp = this.chirpRepository.findOne(chirpId);
		Assert.notNull(chirp);

		return chirp;
	}

	public Chirp save(Chirp chirp) {
		Chirp result;
		Assert.isTrue(LoginService.getPrincipal().equals(chirp.getUser().getUserAccount()), "error.commit.owner");

		chirp.setMoment(new Date(System.currentTimeMillis() - 1000));
		result = this.chirpRepository.save(chirp);

		Assert.notNull(result);
		return result;
	}

	public void delete(Chirp chirp) {
		Assert.notNull(chirp);
		Assert.isTrue(LoginService.getPrincipal().isAuthority("ADMIN"), "error.commit.permission");
		this.chirpRepository.delete(chirp);
	}

	// Other bussines methods ------------------------------ (Otras reglas de negocio, como por ejemplo findRegisteredUser())
	public Collection<Chirp> findChirpsByUserId(int userId) {
		return this.chirpRepository.findChirpsByUserId(userId);
	}

	public Collection<Chirp> findFollowingChirpsByUserId(int userId) {
		return this.chirpRepository.findFollowingChirpsByUserId(userId);
	}

	public Collection<Chirp> getChirpsWithSpamWords() {

		EntityManagerFactory factory = Persistence.createEntityManagerFactory("Acme-Newspaper");

		EntityManager em = factory.createEntityManager();
		FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(em);
		em.getTransaction().begin();

		String regexp = "";
		for (SpamWord sp : this.spamWordService.findAll())
			regexp += sp.getWord() + "|";

		QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Chirp.class).get();
		org.apache.lucene.search.Query luceneQuery = qb.keyword().onFields("title", "description").ignoreFieldBridge().matching(regexp).createQuery();

		javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Chirp.class);

		List result = jpaQuery.getResultList();
		Set<Chirp> cc = new HashSet<>(result);

		em.getTransaction().commit();
		em.close();

		return cc;
	}

	// DASHBOARD
	public Double avgChirpsPerUser() {
		return this.chirpRepository.avgChirpsPerUser();
	}

	public Double stddevChirpsPerUser() {
		return this.chirpRepository.stddevChirpsPerUser();
	}

	public Double ratioUserChirpsUpper75Avg() {
		return this.chirpRepository.ratioUserChirpsUpper75Avg();
	}

	public Chirp reconstruct(Chirp c, BindingResult binding) {
		Chirp result;
		Chirp original = this.chirpRepository.findOne(c.getId());

		if (c.getId() == 0) {
			result = c;
			result.setUser((User) this.actorService.findByPrincipal());
		} else {
			result = this.chirpRepository.findOne(c.getId());

			result.setDescription(c.getDescription());
			result.setTitle(c.getTitle());
		}
		this.validator.validate(result, binding);

		return result;

	}
	
	//Paginated Repositories
	public Page<Chirp> findFollowingChirpsByUserIdPaginate(final Integer pageNumber,
			final Integer pageSize, int userId) {
		final PageRequest request = new PageRequest(pageNumber - 1, pageSize);
		return this.chirpRepository.findFollowingChirpsByUserIdPaginate(userId, request);
	}
}
