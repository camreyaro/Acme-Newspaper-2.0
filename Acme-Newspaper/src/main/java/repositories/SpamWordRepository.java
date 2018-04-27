
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.SpamWord;

public interface SpamWordRepository extends JpaRepository<SpamWord, Integer> {

	@Query("select w from SpamWords w")
	SpamWord getSpamWords();

}
