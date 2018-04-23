
package forms;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

public class CustomerForm {

	private String	userName;
	private String	name;
	private String	surname;
	private String	postalAddress;
	private String	phoneNumber;
	private String	emailAddress;


	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getPostalAddress() {
		return this.postalAddress;
	}

	public void setPostalAddress(final String postalAddress) {
		this.postalAddress = postalAddress;
	}
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@Pattern(regexp = "(^\\+?\\d+)?")
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@NotBlank
	@Email
	public String getEmailAddress() {
		return this.emailAddress;
	}

	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
	}
	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(final String userName) {
		this.userName = userName;
	}

}
