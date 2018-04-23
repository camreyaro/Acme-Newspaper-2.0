
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.FollowUp;

public interface FollowUpRepository extends JpaRepository<FollowUp, Integer> {

	@Query("select f from FollowUp f where f.article.id=?1")
	Collection<FollowUp> getFollowUpsFromArticle(int articleId);
	
	@Query("select count(f)*1.0/(select count(a) from Article a) from FollowUp f where f.moment <= f.article.newspaper.publicationDate + 604800000")
	Collection<FollowUp> getFollowUpsPerArticleUpOneWeek();
	
	@Query("select count(f)*1.0/(select count(a) from Article a) from FollowUp f where f.moment <= f.article.newspaper.publicationDate + 2*604800000")
	Collection<FollowUp> getFollowUpsPerArticleUpTwoWeek();
}
