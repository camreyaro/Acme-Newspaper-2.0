
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Indexed
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "title"), @Index(columnList = "agent_id"), @Index(columnList = "newspaper_id")
})
public class Advertisement extends DomainEntity {

	private String		title;
	private String		urlBanner;
	private String		urlTargetPage;
	private Double		price;
	private CreditCard	creditCard;


	@NotNull
	@Valid
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	@NotBlank
	@Length(max = 200)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@URL
	@NotBlank
	@Pattern(regexp = ".+.(jpg|jpeg|gif|png)", message = "(jpg, jpeg, gif, png)")
	public String getUrlBanner() {
		return this.urlBanner;
	}

	public void setUrlBanner(String urlBanner) {
		this.urlBanner = urlBanner;
	}

	@URL
	@NotBlank
	public String getUrlTargetPage() {
		return this.urlTargetPage;
	}

	public void setUrlTargetPage(String urlTargetPage) {
		this.urlTargetPage = urlTargetPage;
	}

	@NotNull
	@DecimalMin("0.1")
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}


	//---------------Relationships

	private Newspaper	newspaper;
	private Agent		agent;


	@ManyToOne
	public Newspaper getNewspaper() {
		return this.newspaper;
	}

	public void setNewspaper(Newspaper newspaper) {
		this.newspaper = newspaper;
	}

	@ManyToOne
	public Agent getAgent() {
		return this.agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
	}

}
