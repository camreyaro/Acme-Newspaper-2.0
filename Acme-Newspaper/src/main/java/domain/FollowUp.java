
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.persistence.Index;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
		@Index(columnList = "moment"), @Index(columnList = "article_id")
	})
public class FollowUp extends DomainEntity {

	// Propiedades
	private String				title;
	private Date				moment;
	private String				text;
	private Collection<String>	pictureUrls;

	// Relaciones
	private Article				article;


	// --- GETTERS AND SETTERS ---

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(Date moment) {
		this.moment = moment;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@NotNull
//	@SafeHtml(whitelistType = WhiteListType.NONE)
	@ElementCollection
	public Collection<String> getPictureUrls() {
		return this.pictureUrls;
	}

	public void setPictureUrls(Collection<String> pictureUrls) {
		this.pictureUrls = pictureUrls;
	}

	// --- GETTERS AND SETTERS DE RELACIONES ---

	@Valid
	@ManyToOne(optional = false)
	public Article getArticle() {
		return this.article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}
	//@Valid
	//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
}
