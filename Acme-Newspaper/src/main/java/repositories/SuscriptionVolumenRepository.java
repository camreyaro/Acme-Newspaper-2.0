
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.SuscriptionVolumen;

@Repository
public interface SuscriptionVolumenRepository extends JpaRepository<SuscriptionVolumen, Integer> {

}
