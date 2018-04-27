
package services;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SuscriptionVolumenRepository;
import domain.CreditCard;
import domain.Customer;
import domain.Newspaper;
import domain.SuscriptionVolumen;
import domain.Volumen;

@Service
@Transactional
public class SuscriptionVolumenService {

	@Autowired
	private SuscriptionVolumenRepository	suscriptionVolumenRepository;
	@Autowired
	private ActorService					actorService;
	@Autowired
	private Validator						validator;


	public SuscriptionVolumen create(final Volumen v) {
		final SuscriptionVolumen res = new SuscriptionVolumen();

		res.setVolumen(v);
		res.setCustomer((Customer) this.actorService.findByPrincipal());

		return res;
	}

	public SuscriptionVolumen findOne(final Integer id) {
		return this.suscriptionVolumenRepository.findOne(id);
	}

	public void delete(final Integer id) {
		this.suscriptionVolumenRepository.delete(id);
	}

	public SuscriptionVolumen save(final SuscriptionVolumen sv) {
		Assert.isTrue(this.amSubscribed(sv.getVolumen()), "suscriptionVolumen.alreadySubscribed.error");
		Assert.isTrue(this.validCreditCardDate(sv.getCreditCard()), "message.error.creditcard"); //Cc caducada
		Assert.isTrue(sv.getId() == 0, "suscriptionVolumen.edit.error"); // No puedes editar una suscripción maquina

		return this.suscriptionVolumenRepository.save(sv);
	}

	private boolean validCreditCardDate(final CreditCard cc) {
		if (cc.getExpirationYear() > Calendar.getInstance().get(Calendar.YEAR) || (cc.getExpirationYear() == Calendar.getInstance().get(Calendar.YEAR) && cc.getExpirationMonth() >= Calendar.getInstance().get(Calendar.MONTH) + 1))
			return true;
		else
			return false;
	}

	public SuscriptionVolumen reconstruct(final SuscriptionVolumen sv, final BindingResult binding) {
		final SuscriptionVolumen res;

		//TODO:obligar en el controlador a que siempre pase por el if y no por el else
		if (sv.getId() == 0) {
			res = new SuscriptionVolumen();

			res.setCustomer((Customer) this.actorService.findByPrincipal());
			res.setVolumen(sv.getVolumen());

			res.setCreditCard(sv.getCreditCard());

		} else
			res = sv;

		this.validator.validate(res, binding);

		return res;
	}

	public Boolean amSubscribed(final Volumen v) {
		final Customer c = (Customer) this.actorService.findByPrincipal();
		final SuscriptionVolumen sv = this.suscriptionVolumenRepository.getSVFromVolumenAndCustomer(v.getId(), c.getId());

		Boolean res = true;
		if (sv == null)
			res = false;
		return res;
	}

	public Boolean amSubscribed(final Newspaper n) {
		final Customer c = (Customer) this.actorService.findByPrincipal();
		final SuscriptionVolumen sv = this.suscriptionVolumenRepository.getSVFromNewspaperAndCustomer(n.getId(), c.getId());

		Boolean res = true;
		if (sv == null)
			res = false;
		return res;
	}

}
