
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Newspaper;

@Repository
public interface NewspaperRepository extends JpaRepository<Newspaper, Integer> {

	@Query("select count(a) from Article a where a.newspaper.id=?1 and a.saved=false")
	Integer findNotSavedArticlesByNewspaper(int newspaperId); //if notSaved > 0, cannot publish newspaper

	@Query("select n from Newspaper n where n.published=1")
	Collection<Newspaper> findAllPublished();

	@Query("select u.newspapers from User u where u.id=?1")
	Collection<Newspaper> findAllByUser(int userId);

	@Query("select n from Newspaper n where n.published=1 and (n.title LIKE concat(concat('%',?1),'%') or n.description LIKE concat(concat('%',?1),'%'))")
	Collection<Newspaper> findByKeyword(String keyword);
	
	@Query("select n from Newspaper n where n.published=0")
	Collection<Newspaper> findAllAvaibles();

	// ====== DASHBOARD ======

	//C3
	@Query("select avg(n.articles.size) from Newspaper n")
	Double avgArticlesByNewspaper();

	@Query("select stddev(n.articles.size) from Newspaper n")
	Double stddevArticlesByNewspaper();

	//C4
	@Query("select n.title from Newspaper n where n.articles.size > (select 1.1*avg(n.articles.size) from Newspaper n)")
	Collection<Newspaper> getNewspapersUpper10PerCentArticles();

	@Query("select n.title from Newspaper n where n.articles.size < (select 1.1*avg(n.articles.size) from Newspaper n)")
	Collection<Newspaper> getNewspapersLower10PerCentArticles();

	//	@Query("select avg(a.followUps.size) from Newspaper n join n.articles a where n.publicationDate - CURRENT_DATE < 7")
	//	Double avgFollowUpsPerArticleUpTwoWeeks();

	//	@Query("select avg(a.followUps.size) from Newspaper n join n.articles a where n.publicationDate - CURRENT_DATE < 14")
	//	Double avgFollowUpsPerArticleUpTwoWeeks();

	// A1
	@Query("select 1.0*(select count(publicN) from Newspaper publicN where publicN.publicNp = true)/count(privN) from Newspaper privN where privN.publicNp =false")
	Double ratioPublicVsPrivateNewspapers();

	// A2
	@Query("select avg(n.articles.size) from Newspaper n where n.publicNp = false")
	Double avgArticlesPerPrivateNewspapers();

	// A3
	@Query("select avg(n.articles.size) from Newspaper n where n.publicNp = true")
	Double avgArticlesPerPublicNewspapers();
	
	//A4
	@Query("select count(distinct s.customer)*1.0/(select count(n1) * (select count(c) from Customer c) from Newspaper n1 where n1.publicNp is false) from Suscription s")
	Double ratioSuscribersPerPrivateNewspapersVsCustomers();
	
	// A5
	@Query("select count(n1)*1.0/(select count(n2)*1.0 * (select count(u) from User u where u.newspapers is not empty) from Newspaper n2 where n2.publicNp = true) from Newspaper n1 where n1.publicNp = true")
	Double avgRatioPublicVsPrivateNewspapers();
	
	//Paginated repository
	@Query("select n from Newspaper n where n.published=1")
	Page<Newspaper> getPublishedNewspapersPaginate(Pageable p);
	
	@Query("select n from Newspaper n where n.published=1 and (n.title LIKE concat(concat('%',?1),'%') or n.description LIKE concat(concat('%',?1),'%'))")
	Page<Newspaper> findByKeywordPaginate(String keyword, Pageable p);
	
	@Query("select n from User u join u.newspapers n where u.id=?1")
	Page<Newspaper> findAllByUserPaginate(int userId, Pageable p);

}
