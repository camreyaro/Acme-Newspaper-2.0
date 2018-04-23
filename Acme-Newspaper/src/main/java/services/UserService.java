
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

import repositories.UserRepository;
import security.Authority;
import security.UserAccount;
import domain.Newspaper;
import domain.User;
import forms.UserForm;

@Service
@Transactional
public class UserService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private UserRepository	userRepository;

	// Supporting services ----------------------------------------------------
	@Autowired
	private ActorService	actorService;
	@Autowired
	private Validator		validator;


	// Simple CRUD methods ----------------------------------------------------

	public User create() {
		User user;
		UserAccount userAccount;
		Collection<Newspaper> newspapers;
		Collection<Authority> authorities;
		Collection<User> following;
		Authority auth;

		user = new User();
		userAccount = new UserAccount();
		newspapers = new ArrayList<>();
		following = new ArrayList<>();
		following.add(user);
		authorities = new ArrayList<>();
		auth = new Authority();

		user.setNewspapers(newspapers);
		user.setFollowing(following);
		auth.setAuthority("USER");
		authorities.add(auth);
		userAccount.setAuthorities(authorities);
		user.setUserAccount(userAccount);
		user.setHasConfirmedTerms(false);

		return user;
	}

	public void saveFromCreate(User user) {
		Assert.isTrue(this.actorService.findByUserAccountUsername(user.getUserAccount().getUsername()) == null, "user.duplicated.username");

		Md5PasswordEncoder encoder;
		String hash;

		encoder = new Md5PasswordEncoder();
		hash = encoder.encodePassword(user.getUserAccount().getPassword(), null);

		user.getUserAccount().setPassword(hash);

		user.setConfirmMoment(new Date(System.currentTimeMillis()));

		this.userRepository.save(user);

	}

	public void save(User user) {
		Assert.isTrue(this.actorService.findByPrincipal().getUserAccount().equals(user.getUserAccount()), "customer.another.customer");
		this.userRepository.save(user);
	}

	public User findOne(int userId) {
		Assert.notNull(userId);
		User user = this.userRepository.findOne(userId);

		return user;
	}

	public Collection<User> findAll() {
		Collection<User> users = this.userRepository.findAll();

		return users;
	}

	public User reconstruct(UserForm form) {

		User result = (User) this.actorService.findByUserAccountUsername(form.getUsername());

		result.setEmailAddress(form.getEmailAddress());
		result.setName(form.getName());
		result.setSurname(form.getSurname());
		result.setPhoneNumber(form.getPhoneNumber());
		result.setPostalAddress(form.getPostalAddress());

		return result;

	}

	public User reconstruct(User user, BindingResult binding) {

		UserAccount userAccount;
		Collection<Newspaper> newspapers;
		Collection<User> following;
		Collection<Authority> authorities;
		Authority auth;

		userAccount = user.getUserAccount();
		newspapers = new ArrayList<>();
		following = new ArrayList<>();
		authorities = new ArrayList<>();
		auth = new Authority();

		user.setNewspapers(newspapers);
		user.setFollowing(following);
		auth.setAuthority("USER");
		authorities.add(auth);
		userAccount.setAuthorities(authorities);
		user.setUserAccount(userAccount);

		user.setId(0);
		user.setVersion(0);

		this.validator.validate(user, binding);

		return user;

	}

	public void saveAndFlush(User user) {
		this.userRepository.saveAndFlush(user);
	}

	// RED SOCIAL
	public void follow(int userId) {
		User user = this.findOne(userId);

		Collection<User> myFollowings = new ArrayList<>();
		User me = (User) this.actorService.findByPrincipal();
		myFollowings.addAll(me.getFollowing());
		if (!myFollowings.contains(user))
			myFollowings.add(user);
		me.setFollowing(myFollowings);
		this.save(me);
	}
	public void unfollow(int userId) {
		User user = this.findOne(userId);

		Collection<User> myFollowings = new ArrayList<>();
		User me = (User) this.actorService.findByPrincipal();
		myFollowings.addAll(me.getFollowing());
		if (myFollowings.contains(user))
			myFollowings.remove(user);
		me.setFollowing(myFollowings);
		this.save(me);
	}
	public Collection<User> getFollowers(int userId) {
		return this.userRepository.getFollowers(userId);
	}

	// DASHBOARD

	public Double avgNewspapersByWriter() {
		return this.userRepository.avgNewspapersByWriter();
	}

	public Double stddevNewspapersByWriter() {
		return this.userRepository.stddevNewspapersByWriter();
	}

	public Double avgArticlesByWriter() {
		return this.userRepository.avgArticlesByWriter();
	}

	public Double stddevArticlesByWriter() {
		return this.userRepository.stddevArticlesByWriter();
	}

	public Double ratioNewspapersCreated() {
		return this.userRepository.ratioNewspapersCreated();
	}

	public Double ratioArticlesCreated() {
		return this.userRepository.ratioArticlesCreated();
	}
}
