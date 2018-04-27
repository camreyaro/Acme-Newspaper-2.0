
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;

@Service
@Transactional
public class ActorService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ActorRepository	actorRepository;


	// CRUD methods

	public Actor findOne(final int actorId) {
		Assert.notNull(actorId);

		Actor actor;

		actor = this.actorRepository.findOne(actorId);

		return actor;
	}

	// Other business methods -------------------------------------------------

	public Actor findByPrincipal() {
		UserAccount userAccount;
		Actor actor;

		userAccount = LoginService.getPrincipal();
		actor = this.actorRepository.findByUserAccountId(userAccount.getId());

		return actor;
	}

	public Actor findByUserAccountUsername(final String username) {
		Assert.notNull(username);
		final Actor actor = this.actorRepository.findByUserAccountUsername(username);

		return actor;
	}

	public Collection<Actor> findAll() {

		final Collection<Actor> actors = this.actorRepository.findAll();

		return actors;
	}
}
