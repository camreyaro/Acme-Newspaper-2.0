
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.SuscriptionRepository;
import domain.Suscription;

@Component
@Transactional
public class StringToSuscriptionConverter implements Converter<String, Suscription> {

	@Autowired
	SuscriptionRepository	repository;


	@Override
	public Suscription convert(String text) {
		Suscription result;
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
