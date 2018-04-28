
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Past;

@Entity
@Access(AccessType.PROPERTY)
public class Agent extends Actor {

	private Date	acceptedTermsMoment;


	@Past
	public Date getAcceptedTermsMoment() {
		return this.acceptedTermsMoment;
	}

	public void setAcceptedTermsMoment(Date acceptedTermsMoment) {
		this.acceptedTermsMoment = acceptedTermsMoment;
	}

}
