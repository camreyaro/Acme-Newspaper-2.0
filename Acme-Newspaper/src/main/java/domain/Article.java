
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import javax.persistence.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Indexed
@Access(AccessType.PROPERTY)
@Table(indexes = {
		@Index(columnList = "moment"), @Index(columnList = "creator_id"),
		@Index(columnList = "newspaper_id")
	})
public class Article extends DomainEntity {

	//Propiedades
	private Date					moment;
	private String					title;
	private String					summary;
	private String					body;
	private Collection<String>		pictureURLs;
	private Boolean					saved;

	//Relaciones
	private User					creator;
	private Newspaper				newspaper;


	// --- GETTERS AND SETTERS ---

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getMoment() {
		return this.moment;
	}

	public void setMoment(Date moment) {
		this.moment = moment;
	}

	@Field(index=org.hibernate.search.annotations.Index.YES, analyze=Analyze.YES, store=Store.NO)
	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Lob
	@NotBlank
	@Field(index=org.hibernate.search.annotations.Index.YES, analyze=Analyze.YES, store=Store.NO)
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@NotBlank
	@Field(index=org.hibernate.search.annotations.Index.YES, analyze=Analyze.YES, store=Store.NO)
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Lob
	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@NotNull
	@IndexedEmbedded
	@ElementCollection
	public Collection<String> getPictureURLs() {
		return this.pictureURLs;
	}

	public void setPictureURLs(Collection<String> pictureURLs) {
		this.pictureURLs = pictureURLs;
	}

	public Boolean getSaved() {
		return this.saved;
	}

	public void setSaved(Boolean saved) {
		this.saved = saved;
	}

	// --- GETTERS AND SETTERS DE RELACIONES --- 

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public User getCreator() {
		return this.creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Newspaper getNewspaper() {
		return this.newspaper;
	}

	public void setNewspaper(Newspaper newspaper) {
		this.newspaper = newspaper;
	}
	
	@Transient
	public String getReductedSummary(){
		String reducted = "";
		if(summary.length() >= 50){
			reducted = summary.substring(0, 50) + "...";
		}else{
			reducted = summary;
		}
		
		return reducted;
	}
	
	@Transient
	public Boolean canDisplay(){
		return newspaper.getPublished() && saved;
	}

}
