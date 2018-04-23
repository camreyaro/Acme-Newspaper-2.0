package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.Entity;
import javax.persistence.AccessType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {
	
	
	// RELATIONSHIPS -----------------------------------------
	private Collection<Newspaper> newspapers;
	private Collection<User> following;

	
	@Valid
	@OneToMany
	public Collection<Newspaper> getNewspapers() {
		return newspapers;
	}

	public void setNewspapers(Collection<Newspaper> newspapers) {
		this.newspapers = newspapers;
	}

	@Valid
	@ManyToMany
	public Collection<User> getFollowing() {
		return following;
	}

	public void setFollowing(Collection<User> following) {
		this.following = following;
	}
	
	
	

}
