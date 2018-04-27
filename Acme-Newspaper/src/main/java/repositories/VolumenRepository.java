
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Volumen;

@Repository
public interface VolumenRepository extends JpaRepository<Volumen, Integer> {

}
