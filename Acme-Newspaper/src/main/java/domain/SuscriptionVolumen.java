
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class SuscriptionVolumen extends DomainEntity {

	private CreditCard	creditCard;

	private Volumen		volumen;
	private Customer	customer;


	@Valid
	@NotNull
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}
	@Valid
	@ManyToOne(optional = false)
	public Volumen getVolumen() {
		return this.volumen;
	}

	public void setVolumen(final Volumen volumen) {
		this.volumen = volumen;
	}

	@Valid
	@ManyToOne(optional = false)
	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(final Customer customer) {
		this.customer = customer;
	}

}
