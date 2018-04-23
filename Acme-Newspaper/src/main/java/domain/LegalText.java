package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class LegalText extends DomainEntity{
	
	private String type;
	private String codeLanguage;
	private String body;
	
	
	@NotBlank
	@Pattern(regexp = "(^TERMS|COOKIES$)")
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	@NotBlank
	public String getCodeLanguage() {
		return codeLanguage;
	}
	
	public void setCodeLanguage(String codeLanguage) {
		this.codeLanguage = codeLanguage;
	}
	
	@Lob
	@NotBlank
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	

}
