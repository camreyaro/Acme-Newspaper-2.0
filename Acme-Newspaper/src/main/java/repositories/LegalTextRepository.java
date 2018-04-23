package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.LegalText;

public interface LegalTextRepository extends JpaRepository<LegalText, Integer> {
	
	@Query("select lt from LegalText lt where lt.type='TERMS' and lt.codeLanguage = 'es'")
	LegalText getTermsAndConditionsES();
	
	@Query("select lt from LegalText lt where lt.type='TERMS' and lt.codeLanguage = 'en'")
	LegalText getTermsAndConditionsEN();
	
	@Query("select lt from LegalText lt where lt.type='COOKIES' and lt.codeLanguage = 'es'")
	LegalText getCookiesES();
	
	@Query("select lt from LegalText lt where lt.type='COOKIES' and lt.codeLanguage = 'en'")
	LegalText getCookiesEN();

}
