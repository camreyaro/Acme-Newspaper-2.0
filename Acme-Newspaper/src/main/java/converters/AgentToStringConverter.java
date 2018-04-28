
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Agent;

@Component
@Transactional
public class AgentToStringConverter implements Converter<Agent, String> {

	@Override
	public String convert(Agent o) {
		String result;

		if (o == null)
			result = null;
		else
			result = String.valueOf(o.getId());

		return result;
	}

}
