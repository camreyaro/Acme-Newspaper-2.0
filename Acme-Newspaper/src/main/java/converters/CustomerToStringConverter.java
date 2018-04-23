
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Customer;

@Component
@Transactional
public class CustomerToStringConverter implements Converter<Customer, String> {

	@Override
	public String convert(Customer o) {
		String result;

		if (o == null)
			result = null;
		else
			result = String.valueOf(o.getId());

		return result;
	}

}
