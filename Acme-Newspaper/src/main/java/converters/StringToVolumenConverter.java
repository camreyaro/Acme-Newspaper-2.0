
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.VolumenRepository;
import domain.Volumen;

@Component
@Transactional
public class StringToVolumenConverter implements Converter<String, Volumen> {

	@Autowired
	VolumenRepository	repository;


	@Override
	public Volumen convert(final String text) {
		Volumen result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.repository.findOne(id);
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
