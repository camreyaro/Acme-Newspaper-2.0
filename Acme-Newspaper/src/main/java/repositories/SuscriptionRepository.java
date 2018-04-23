
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Suscription;

@Repository
public interface SuscriptionRepository extends JpaRepository<Suscription, Integer> {

	@Query("select count(a) from Suscription a WHERE a.customer.id=?1 and a.newspaper.id=?2")
	Integer customerSuscribe(int customerId, int newspaperId);

	@Query("select a from Suscription a WHERE a.customer.id=?1")
	Collection<Suscription> suscriptionFromCustomer(int customerId);
	
	@Query("select a from Suscription a WHERE a.newspaper.id=?1")
	Collection<Suscription> suscriptionByNewspaperId(int newspaperId);
}
