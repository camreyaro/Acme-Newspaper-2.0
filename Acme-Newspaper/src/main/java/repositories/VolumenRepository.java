
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;
import domain.Volumen;

@Repository
public interface VolumenRepository extends JpaRepository<Volumen, Integer> {

	@Query("select n from Volumen v join v.newspapers n where n.publicNp=1 and v.id=?1")
	Collection<Newspaper> getPublicNewspaper(Integer id);

	@Query("select n from Volumen v join v.newspapers n where n.publicNp=0 and v.id=?1")
	Collection<Newspaper> getPrivateNewspaper(Integer id);

}
