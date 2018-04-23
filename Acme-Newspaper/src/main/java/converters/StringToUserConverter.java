/*
 * StringToUserConverter.java
 * 
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.UserRepository;
import domain.User;

@Component
@Transactional
public class StringToUserConverter implements Converter<String, User> {

	@Autowired
	UserRepository	userRepository;


	@Override
	public User convert(String text) {
		User result;
		int id;

		try {
			id = Integer.valueOf(text);
			result = this.userRepository.findOne(id);
		} catch (Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
	

}
