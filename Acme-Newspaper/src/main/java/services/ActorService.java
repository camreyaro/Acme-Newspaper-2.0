package services;

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
	private ActorRepository actorRepository;

	// CRUD methods

	public Actor findOne(int actorId) {
		Assert.notNull(actorId);

		Actor actor;

		actor = actorRepository.findOne(actorId);

		return actor;
	}

	// Other business methods -------------------------------------------------

	public Actor findByPrincipal() {
		UserAccount userAccount;
		Actor actor;

		userAccount = LoginService.getPrincipal();
		actor = actorRepository.findByUserAccountId(userAccount.getId());

		return actor;
	}
	
	public Actor findByUserAccountUsername(String username){
		Assert.notNull(username);
		Actor actor = actorRepository.findByUserAccountUsername(username);
		
		return actor;
	}

}
