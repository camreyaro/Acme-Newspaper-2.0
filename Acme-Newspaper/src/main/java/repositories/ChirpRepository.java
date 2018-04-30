
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chirp;

@Repository
public interface ChirpRepository extends JpaRepository<Chirp, Integer> {

	@Query("select c from Chirp c where c.user.id=?1")
	Collection<Chirp> findChirpsByUserId(int userId);

	@Query("select c from Chirp c where c.user in (select f from User x join x.following f where x.id=?1) order by c.moment desc")
	Collection<Chirp> findFollowingChirpsByUserId(int userId);

	//Dashboard

	@Query("select 1.0*(select count(c) from Chirp c)/count(u) from User u")
	Double avgChirpsPerUser();
	

	@Query("select stddev(1.0*(select count(c) from Chirp c where c.user = u)) from User u")
	Double stddevChirpsPerUser();
	
	@Query("select (select 1.0*(count(u)/count(z)) from User u where (select count(c) from Chirp c where c.user=u)> 0.75*(select avg(1.0*(select count(h) from Chirp h where h.user = x)) from User x)) from User z")
	Double ratioUserChirpsUpper75Avg();
	
	//Paginated repository
	@Query("select c from Chirp c where c.user in (select f from User x join x.following f where x.id=?1) order by c.moment desc")
	Page<Chirp> findFollowingChirpsByUserIdPaginate(int userId, Pageable p);

}
