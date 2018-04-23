package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class SpamWord extends DomainEntity{

		private String word;

		
		@NotBlank
		public String getWord() {
			return word;
		}

		public void setWord(String word) {
			this.word = word;
		}
		
		
}
