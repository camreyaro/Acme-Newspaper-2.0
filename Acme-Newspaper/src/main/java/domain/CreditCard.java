
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Embeddable
@Access(AccessType.PROPERTY)
public class CreditCard {

	private String	number;
	private String	holderName;
	private Integer	cvvCode;
	private String	brandName;
	private Integer	expirationMonth;
	private Integer	expirationYear;


	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	@Pattern(regexp = "^[0-9]{16}")
	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotBlank
	public String getHolderName() {
		return this.holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	@NotNull
	@Range(min = 100, max = 999)
	public Integer getCvvCode() {
		return this.cvvCode;
	}

	public void setCvvCode(Integer cvvCode) {
		this.cvvCode = cvvCode;
	}

	@NotBlank
	public String getBrandName() {
		return this.brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	@Range(min = 1, max = 12)
	public Integer getExpirationMonth() {
		return this.expirationMonth;
	}

	public void setExpirationMonth(Integer expirationMonth) {
		this.expirationMonth = expirationMonth;
	}
	@Range(min = 1950, max = 3000)
	public Integer getExpirationYear() {
		return this.expirationYear;
	}

	public void setExpirationYear(Integer expirationYear) {
		this.expirationYear = expirationYear;
	}

}
