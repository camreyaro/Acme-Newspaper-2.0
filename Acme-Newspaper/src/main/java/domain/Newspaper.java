
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Indexed
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "published"), @Index(columnList = "title, description"), @Index(columnList = "publisher_id")
})
public class Newspaper extends DomainEntity {

	// Attributes
	private String	title;
	private Date	publicationDate;
	private String	description;
	private String	pictureUrl;
	private Boolean	published;
	private Boolean	publicNp;
	private Double	price;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@Temporal(TemporalType.DATE)
	public Date getPublicationDate() {
		return this.publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Field(index = org.hibernate.search.annotations.Index.YES, analyze = Analyze.YES, store = Store.NO)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	@URL
	@IndexedEmbedded
	public String getPictureUrl() {
		return this.pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	@NotNull
	public Boolean getPublished() {
		return this.published;
	}

	public void setPublished(Boolean published) {
		this.published = published;
	}

	@NotNull
	public Boolean getPublicNp() {
		return this.publicNp;
	}

	public void setPublicNp(Boolean publicNp) {
		this.publicNp = publicNp;
	}

	@NotNull
	@DecimalMin("0.0")
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}


	// Relationships
	private Collection<Article>	articles;
	private User				publisher;


	@NotNull
	@Valid
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "newspaper")
	public Collection<Article> getArticles() {
		return this.articles;
	}

	public void setArticles(Collection<Article> articles) {
		this.articles = articles;
	}

	@ManyToOne
	public User getPublisher() {
		return this.publisher;
	}

	public void setPublisher(User publisher) {
		this.publisher = publisher;
	}

}
