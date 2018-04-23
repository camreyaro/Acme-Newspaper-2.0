
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Suscription;

@Component
@Transactional
public class SuscriptionToStringConverter implements Converter<Suscription, String> {

	@Override
	public String convert(Suscription o) {
		String result;

		if (o == null)
			result = null;
		else
			result = String.valueOf(o.getId());

		return result;
	}

}
