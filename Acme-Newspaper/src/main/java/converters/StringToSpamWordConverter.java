
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.SpamWordRepository;
import domain.SpamWord;

@Component
@Transactional
public class StringToSpamWordConverter implements Converter<String, SpamWord> {

	@Autowired
	SpamWordRepository	repository;


	@Override
	public SpamWord convert(String text) {
		SpamWord result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.repository.findOne(id);
		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
