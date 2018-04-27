
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Folder extends DomainEntity {

	private String				name;
	private Boolean				predefined;

	//Relaciones--------------------------------

	private Collection<Folder>	children;
	private Folder				parent;
	//private Collection<Message>	messages;
	private Actor				actor;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	@NotNull
	public Boolean getPredefined() {
		return this.predefined;
	}

	public void setPredefined(final Boolean predefined) {
		this.predefined = predefined;
	}

	// RelationShips---------------------------------
	/*
	 * @NotNull
	 * 
	 * @OneToMany(mappedBy = "folder")
	 * public Collection<Message> getMessages() {
	 * return this.messages;
	 * }
	 * 
	 * public void setMessages(final Collection<Message> messages) {
	 * this.messages = messages;
	 * }
	 */
	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Actor getActor() {
		return this.actor;
	}

	public void setActor(final Actor actor) {
		this.actor = actor;
	}
	@NotNull
	@OneToMany
	public Collection<Folder> getChildren() {
		return this.children;
	}

	public void setChildren(final Collection<Folder> children) {
		this.children = children;
	}
	@Valid
	@ManyToOne(optional = true)
	public Folder getParent() {
		return this.parent;
	}

	public void setParent(final Folder parent) {
		this.parent = parent;
	}
}
