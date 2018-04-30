
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("select u from User u join u.following f where (select m from User m where m.id=?1) in f")
	Collection<User> getFollowers(int userId);

	//==== DASHBOARD ====

	//C1
	@Query("select avg(u.newspapers.size) from User u")
	Double avgNewspapersByWriter();

	@Query("select stddev(u.newspapers.size) from User u")
	Double stddevNewspapersByWriter();

	//C2
	@Query("select avg(n.articles.size) from User u join u.newspapers n")
	Double avgArticlesByWriter();

	@Query("select stddev(n.articles.size) from User u join u.newspapers n")
	Double stddevArticlesByWriter();

	//C6
	@Query("select 1.0*(select count(distinct n.publisher) from Newspaper n)/count(u) from User u")
	Double ratioNewspapersCreated();

	//C7
	@Query("select 1.0*(select count(distinct a.creator) from Article a)/count(u) from User u")
	Double ratioArticlesCreated();
	
	@Query("select u from User u")
	Page<User> findAllPaginate(Pageable p);

	//B5
//	@Query("")
//	Double ratioUserChirpsAbove75();
	
}
