
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.AdvertisementRepository;
import domain.Advertisement;

@Component
@Transactional
public class StringToAdvertisementConverter implements Converter<String, Advertisement> {

	@Autowired
	AdvertisementRepository	repository;


	@Override
	public Advertisement convert(String text) {
		Advertisement result;
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
