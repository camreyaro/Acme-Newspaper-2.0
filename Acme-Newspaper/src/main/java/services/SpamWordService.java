package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SpamWordRepository;
import domain.SpamWord;

@Service
@Transactional
public class SpamWordService {

	// Used repository
	@Autowired
	private SpamWordRepository spamWordRepository;

	// Simple CRUD methods

	public SpamWord create() {
		SpamWord spamWord;

		spamWord = new SpamWord();

		return spamWord;
	}

	public SpamWord save(SpamWord spamWord) {
		Assert.notNull(spamWord);
		SpamWord spamWordSaved;

		if (spamWord.getId() == 0) {
			for (SpamWord s : this.findAll()) {
				Assert.isTrue(!s.getWord().equals(spamWord.getWord()),"spamword.word.exists");
			}
		}
		spamWordSaved = spamWordRepository.save(spamWord);

		return spamWordSaved;
	}

	public SpamWord findOne(int spamWordId) {
		Assert.notNull(spamWordId);
		SpamWord spamWord;

		spamWord = spamWordRepository.findOne(spamWordId);

		return spamWord;
	}

	public Collection<SpamWord> findAll() {
		Collection<SpamWord> spamWords;

		spamWords = spamWordRepository.findAll();

		return spamWords;
	}

	public void delete(SpamWord spamWord) {
		Assert.notNull(spamWord);

		spamWordRepository.delete(spamWord.getId());
	}
	// Other business methods

}
