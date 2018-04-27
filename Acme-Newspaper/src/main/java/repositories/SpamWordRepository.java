
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.SpamWord;

@Repository
public interface SpamWordRepository extends JpaRepository<SpamWord, Integer> {

	@Query("select w from SpamWord w")
	Collection<SpamWord> getSpamWords();

}
