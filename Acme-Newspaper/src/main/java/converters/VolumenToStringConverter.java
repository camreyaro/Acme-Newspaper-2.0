
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Volumen;

@Component
@Transactional
public class VolumenToStringConverter implements Converter<Volumen, String> {

	@Override
	public String convert(final Volumen o) {
		String result;

		if (o == null)
			result = null;
		else
			result = String.valueOf(o.getId());

		return result;
	}

}
