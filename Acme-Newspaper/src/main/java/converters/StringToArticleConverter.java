
package converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.ArticleRepository;
import domain.Article;

@Component
@Transactional
public class StringToArticleConverter implements Converter<String, Article> {

	@Autowired
	ArticleRepository	repository;


	@Override
	public Article convert(String text) {
		Article result;
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
