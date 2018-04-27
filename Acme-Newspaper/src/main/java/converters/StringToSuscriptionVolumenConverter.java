
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.SuscriptionVolumenRepository;
import domain.SuscriptionVolumen;

@Component
@Transactional
public class StringToSuscriptionVolumenConverter implements Converter<String, SuscriptionVolumen> {

	@Autowired
	SuscriptionVolumenRepository	repository;


	@Override
	public SuscriptionVolumen convert(final String text) {
		SuscriptionVolumen result;
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
