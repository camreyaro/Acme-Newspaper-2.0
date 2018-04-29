
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AgentRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Agent;
import forms.AgentForm;

@Service
@Transactional
public class AgentService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private AgentRepository	agentRepository;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private Validator		validator;


	// Supporting services ----------------------------------------------------

	// Simple CRUD methods ----------------------------------------------------

	public Agent create() {
		Agent agent;
		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authorities;

		agent = new Agent();
		userAccount = new UserAccount();
		authorities = new ArrayList<>();
		auth = new Authority();

		auth.setAuthority("AGENT");
		authorities.add(auth);
		userAccount.setAuthorities(authorities);
		agent.setUserAccount(userAccount);
		agent.setHasConfirmedTerms(false);

		return agent;
	}

	public void saveFromCreate(Agent agent) {
		Assert.isTrue(this.actorService.findByUserAccountUsername(agent.getUserAccount().getUsername()) == null, "agent.duplicated.username");

		Md5PasswordEncoder encoder;
		String hash;

		encoder = new Md5PasswordEncoder();
		hash = encoder.encodePassword(agent.getUserAccount().getPassword(), null);

		agent.getUserAccount().setPassword(hash);

		agent.setConfirmMoment(new Date(System.currentTimeMillis()));

		this.agentRepository.save(agent);

	}

	public void save(Agent agent) {
		Assert.isTrue(this.actorService.findByPrincipal().getUserAccount().getId() == agent.getUserAccount().getId(), "agent.another.agent");
		this.agentRepository.save(agent);
	}

	public Agent findOne(int agentId) {
		Assert.notNull(agentId);
		Agent user = this.agentRepository.findOne(agentId);

		return user;
	}

	public Collection<Agent> findAll() {
		Collection<Agent> agents = this.agentRepository.findAll();

		return agents;
	}

	public Agent reconstruct(AgentForm form) {

		Agent result = (Agent) this.actorService.findByUserAccountUsername(form.getUserName());

		result.setEmailAddress(form.getEmailAddress());
		result.setName(form.getName());
		result.setSurname(form.getSurname());
		result.setPhoneNumber(form.getPhoneNumber());
		result.setPostalAddress(form.getPostalAddress());

		return result;

	}

	public Agent reconstruct(Agent agent, BindingResult binding) {

		UserAccount userAccount;
		Collection<Authority> authorities;
		Authority auth;

		userAccount = agent.getUserAccount();
		authorities = new ArrayList<>();
		auth = new Authority();

		auth.setAuthority("AGENT");
		authorities.add(auth);
		userAccount.setAuthorities(authorities);
		agent.setUserAccount(userAccount);

		agent.setId(0);
		agent.setVersion(0);

		this.validator.validate(agent, binding);

		return agent;

	}

	//--------------Others

	public Actor findByUserAccountUsername(String username) {
		Assert.notNull(username);
		Agent agent = (Agent) this.actorService.findByUserAccountUsername(username);

		return agent;
	}

	public void saveAndFlush(Agent agent) {
		this.agentRepository.saveAndFlush(agent);
	}

}
