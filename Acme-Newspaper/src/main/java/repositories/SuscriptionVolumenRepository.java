
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.SuscriptionVolumen;

@Repository
public interface SuscriptionVolumenRepository extends JpaRepository<SuscriptionVolumen, Integer> {

	@Query("select sv from SuscriptionVolumen sv where sv.volumen.id=?1 and sv.customer.id=?2")
	SuscriptionVolumen getSVFromVolumenAndCustomer(Integer volumenId, Integer customerId);

	@Query("select sv from SuscriptionVolumen sv where sv.customer.id=?1 AND (select n from Newspaper n where n.id=?2) member of sv.volumen.newspapers")
	Collection<SuscriptionVolumen> getSVFromNewspaperAndCustomer(Integer customerId, Integer newspaperId);

	@Query("select 1.0*(count(sv)/((select count(s) from Suscription s) + count(sv))) from SuscriptionVolumen sv")
	Double ratioSuscriptionVolumen();

}
