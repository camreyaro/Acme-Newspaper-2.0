
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Chirp;

@Component
@Transactional
public class ChirpToStringConverter implements Converter<Chirp, String> {

	@Override
	public String convert(Chirp o) {
		String result;

		if (o == null)
			result = null;
		else
			result = String.valueOf(o.getId());

		return result;
	}

}
