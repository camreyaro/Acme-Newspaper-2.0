
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.SuscriptionVolumen;

@Component
@Transactional
public class SuscriptionVolumenToStringConverter implements Converter<SuscriptionVolumen, String> {

	@Override
	public String convert(final SuscriptionVolumen o) {
		String result;

		if (o == null)
			result = null;
		else
			result = String.valueOf(o.getId());

		return result;
	}

}
