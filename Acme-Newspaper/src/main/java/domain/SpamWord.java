package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class SpamWord extends DomainEntity{

		private String word;

		
		@NotBlank
		@SafeHtml(whitelistType = WhiteListType.NONE)
		public String getWord() {
			return word;
		}

		public void setWord(String word) {
			this.word = word;
		}
		
		
}
