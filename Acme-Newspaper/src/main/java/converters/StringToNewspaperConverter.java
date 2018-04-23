
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.NewspaperRepository;
import domain.Newspaper;

@Component
@Transactional
public class StringToNewspaperConverter implements Converter<String, Newspaper> {

	@Autowired
	NewspaperRepository	repository;


	@Override
	public Newspaper convert(String text) {
		Newspaper result;
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
