
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Volumen extends DomainEntity {

	private String					title;
	private String					description;
	private Integer					year;
	private Double					price;

	//relations

	private User					user;
	private Collection<Newspaper>	newspapers;


	@SafeHtml
	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}
	@SafeHtml
	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
	@NotNull
	public Integer getYear() {
		return this.year;
	}

	public void setYear(final Integer year) {
		this.year = year;
	}
	@NotNull
	@DecimalMin("0.01")
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(final Double price) {
		this.price = price;
	}
	@Valid
	@ManyToOne(optional = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}
	@Valid
	@ManyToMany
	@NotNull
	public Collection<Newspaper> getNewspapers() {
		return this.newspapers;
	}

	public void setNewspapers(final Collection<Newspaper> newspapers) {
		this.newspapers = newspapers;
	}

}
