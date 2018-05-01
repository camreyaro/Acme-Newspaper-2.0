
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

import repositories.CustomerRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Customer;
import forms.CustomerForm;

@Service
@Transactional
public class CustomerService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CustomerRepository	customerRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private SuscriptionService	suscriptionService;

	@Autowired
	private FolderService		folderService;

	@Autowired
	private Validator			validator;


	// Supporting services ----------------------------------------------------

	// Simple CRUD methods ----------------------------------------------------

	public Customer create() {
		Customer customer;
		UserAccount userAccount;
		Authority auth;
		Collection<Authority> authorities;

		customer = new Customer();
		userAccount = new UserAccount();
		authorities = new ArrayList<>();
		auth = new Authority();

		auth.setAuthority("CUSTOMER");
		authorities.add(auth);
		userAccount.setAuthorities(authorities);
		customer.setUserAccount(userAccount);
		customer.setHasConfirmedTerms(false);

		return customer;
	}
	public Customer save2(final Customer customer) {
		Customer result;
		result = this.customerRepository.save(customer);
		return result;
	}

	public void saveFromCreate(final Customer customer) {
		Customer result;
		Assert.isTrue(this.actorService.findByUserAccountUsername(customer.getUserAccount().getUsername()) == null, "customer.duplicated.username");

		Md5PasswordEncoder encoder;
		String hash;

		encoder = new Md5PasswordEncoder();
		hash = encoder.encodePassword(customer.getUserAccount().getPassword(), null);

		customer.getUserAccount().setPassword(hash);

		customer.setConfirmMoment(new Date(System.currentTimeMillis()));

		//this.customerRepository.save(customer);

		result = this.save2(customer);

		this.folderService.createSystemFolders(result);

	}

	public void save(final Customer customer) {
		Assert.isTrue(this.actorService.findByPrincipal().getUserAccount().getId() == customer.getUserAccount().getId(), "customer.another.customer");
		this.customerRepository.save(customer);
	}

	public Customer findOne(final int customerId) {
		Assert.notNull(customerId);
		final Customer user = this.customerRepository.findOne(customerId);

		return user;
	}

	public Collection<Customer> findAll() {
		final Collection<Customer> customers = this.customerRepository.findAll();

		return customers;
	}

	public Customer reconstruct(final CustomerForm form) {

		final Customer result = (Customer) this.actorService.findByUserAccountUsername(form.getUserName());

		result.setEmailAddress(form.getEmailAddress());
		result.setName(form.getName());
		result.setSurname(form.getSurname());
		result.setPhoneNumber(form.getPhoneNumber());
		result.setPostalAddress(form.getPostalAddress());

		return result;

	}

	public Customer reconstruct(final Customer customer, final BindingResult binding) {

		UserAccount userAccount;
		Collection<Authority> authorities;
		Authority auth;

		userAccount = customer.getUserAccount();
		authorities = new ArrayList<>();
		auth = new Authority();

		auth.setAuthority("CUSTOMER");
		authorities.add(auth);
		userAccount.setAuthorities(authorities);
		customer.setUserAccount(userAccount);

		customer.setId(0);
		customer.setVersion(0);

		this.validator.validate(customer, binding);

		return customer;

	}

	//--------------Others

	public Actor findByUserAccountUsername(final String username) {
		Assert.notNull(username);
		final Customer customer = (Customer) this.actorService.findByUserAccountUsername(username);

		return customer;
	}

	public void saveAndFlush(final Customer customer) {
		this.customerRepository.saveAndFlush(customer);
	}

}
