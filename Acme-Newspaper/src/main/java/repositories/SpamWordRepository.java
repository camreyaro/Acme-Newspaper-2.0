package repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import domain.SpamWord;

public interface SpamWordRepository extends JpaRepository<SpamWord, Integer> {

}
