
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Newspaper;

@Component
@Transactional
public class NewspaperToStringConverter implements Converter<Newspaper, String> {

	@Override
	public String convert(Newspaper o) {
		String result;

		if (o == null)
			result = null;
		else
			result = String.valueOf(o.getId());

		return result;
	}

}
