package converters;

import java.net.URLDecoder;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import security.Authority;

@Component
@Transactional
public class StringToAuthorityConverter implements Converter<String, Authority> {
	
	@Override
	public Authority convert(String text) {
		Authority result;

		if(text==null){
			result = null;
		}
		
		else{
		try {
			result=new Authority();
			result.setAuthority(URLDecoder.decode(text,"UTF-8"));
		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		
	}
		return result;
	}

}
