
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SuscriptionRepository;
import domain.CreditCard;
import domain.Customer;
import domain.Newspaper;
import domain.Suscription;

@Service
@Transactional
public class SuscriptionService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private SuscriptionRepository		suscriptionRepository;

	@Autowired
	private ActorService				actorService;

	@Autowired
	private NewspaperService			newspaperService;
	@Autowired
	private SuscriptionVolumenService	suscriptionVolumenService;

	@Autowired
	Validator							validator;


	// Supporting services ----------------------------------------------------

	// Simple CRUD methods ----------------------------------------------------

	public Suscription create() {
		Suscription suscription;
		Customer customer;
		CreditCard creditCard;

		customer = (Customer) this.actorService.findByPrincipal();
		creditCard = new CreditCard();
		suscription = new Suscription();

		suscription.setCreditCard(creditCard);
		suscription.setCustomer(customer);

		return suscription;
	}

	public void saveFromCreate(final Suscription suscription) {

		this.save(suscription);

	}

	public void save(final Suscription suscription) {
		final boolean res = this.isCustomerSuscribe(Integer.toString(suscription.getNewspaper().getId()));
		Assert.isTrue(res == false, "suscription.duplicate");
		this.suscriptionRepository.save(suscription);
	}

	public Suscription findOne(final int suscriptionId) {
		Assert.notNull(suscriptionId);
		final Suscription suscription = this.suscriptionRepository.findOne(suscriptionId);

		return suscription;
	}

	public Collection<Suscription> findAll() {
		final Collection<Suscription> suscriptions = this.suscriptionRepository.findAll();

		return suscriptions;
	}

	public void suscribe(final Suscription suscription) {
		//En el suscript, vamos a hacer tambien el save de la suscripcion
		Assert.isTrue(this.validCreditCardDate(suscription.getCreditCard()), "message.error.creditcard");

		Newspaper newspaper;
		newspaper = suscription.getNewspaper();
		Assert.isTrue(newspaper.getPublicNp() == false, "message.error.customer.publicNewspaper");
		final boolean res = this.isCustomerSuscribe(Integer.toString(suscription.getNewspaper().getId()));
		Assert.isTrue(res == false, "message.error.suscription.duplicate");
		this.save(suscription);

	}

	public Suscription reconstruct(final Suscription s, final BindingResult binding) {
		Suscription result;

		if (s.getId() == 0) {
			result = s;
			result.setCustomer((Customer) this.actorService.findByPrincipal());
		} else {
			result = this.suscriptionRepository.findOne(s.getId());
			Assert.notNull(result);
			result.setCreditCard(s.getCreditCard());
			result.setCustomer(s.getCustomer());
			result.setNewspaper(s.getNewspaper());
		}
		this.validator.validate(result, binding);

		return result;

	}
	//--------------Others
	private boolean validCreditCardDate(final CreditCard cc) {
		if (cc.getExpirationYear() > Calendar.getInstance().get(Calendar.YEAR) || (cc.getExpirationYear() == Calendar.getInstance().get(Calendar.YEAR) && cc.getExpirationMonth() >= Calendar.getInstance().get(Calendar.MONTH) + 1))
			return true;
		else
			return false;
	}

	public boolean isCustomerSuscribe(final String newspaperId) {
		boolean res = false;
		int customerId;
		Integer query;
		int id;

		id = Integer.valueOf(newspaperId);
		customerId = this.actorService.findByPrincipal().getId();

		query = this.suscriptionRepository.customerSuscribe(customerId, id);

		if (query == 0)
			res = false;
		else if (query >= 1)
			res = true;
		if (!res)
			res = this.suscriptionVolumenService.amSubscribed(id);

		return res;
	}

	public void delete(final int suscriptionId) {
		Assert.notNull(suscriptionId);

		final Suscription s = this.findOne(suscriptionId);
		this.suscriptionRepository.delete(s);
	}

	public Collection<Suscription> suscriptionFromCustomer() {
		int customerId;
		Collection<Suscription> suscriptions = new ArrayList<Suscription>();
		customerId = this.actorService.findByPrincipal().getId();

		suscriptions = this.suscriptionRepository.suscriptionFromCustomer(customerId);

		return suscriptions;
	}

	public void saveAndFlush(final Suscription suscription) {
		this.suscriptionRepository.saveAndFlush(suscription);
	}

	public Collection<Suscription> suscriptionByNewspaperId(final int newspaperId) {
		return this.suscriptionRepository.suscriptionByNewspaperId(newspaperId);
	}
}
