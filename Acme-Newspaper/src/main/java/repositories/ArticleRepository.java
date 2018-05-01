
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Article;
import domain.Newspaper;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

	@Query("select a from Article a where a.creator.id=?1")
	Collection<Article> getAllArticlesByUserId(int id);

	@Query("select a from Article a where a.creator.id =?1 and a.newspaper.published=1 and a.newspaper.publicNp = 1")
	Collection<Article> getPublishedArticlesByUserId(int id);

	@Query("select a from Article a where a.newspaper.published=1 AND (a.newspaper.publicNp=1)")
	Collection<Article> findAllValidAndPublic();

	// Articulos privados y publicados de un customer
	@Query("select a from Suscription s join s.newspaper n join n.articles a where s.customer.id = ?1 and a.saved = true")
	Collection<Article> getPrivatePublishedArticlesByCustomerId(int id);
	// Articulos privados que ha publicado un user
	@Query("select a from Article a join a.newspaper n where a.creator.id = ?1 and n.published = 1 and n.publicNp=0")
	Collection<Article> getAllPrivatePublishedArticlesByUserId(int userId);

	// Articulos privados publicados que no he comprado escritos por un Usuario X siendo yo un Customer Y
	@Query("select a from Article a where a.newspaper.publicNp = 0 and a.newspaper.published = 1 and a.creator.id = ?1 and a.newspaper not in (select s.newspaper from Suscription s where s.customer.id =?2) and a.newspaper not in(select distinct(p) from SuscriptionVolumen sv join sv.volumen.newspapers p where sv.customer.id=?2 )")
	Collection<Article> getPrivatePublishedNotSuscribedArticlesByUserId(int userId, int customerId);

	@Query("select a from Article a where a.newspaper.published = 1 AND (a.title LIKE concat(concat('%',?1),'%') OR a.body LIKE concat(concat('%',?1),'%') OR a.summary LIKE concat(concat('%',?1),'%')) AND  ( (a.newspaper.publicNp=1) OR (a.newspaper.publicNp= 0 AND  (a.newspaper in (select s.newspaper from Suscription s where s.customer.id =?2)))  )")
	Collection<Article> findSuscriptedArticlesByKeyword(String keyword, int actorId);
	
	
	// Articulos privados publicados que he comprado escritos por un Usuario X siendo yo un customer Y
	@Query("select a from Article a where a.newspaper.publicNp = 0 and a.newspaper.published = 1 and a.creator.id = ?1 and (a.newspaper in (select s.newspaper from Suscription s where s.customer.id =?2) OR a.newspaper in(select distinct(p) from SuscriptionVolumen sv join sv.volumen.newspapers p where sv.customer.id=?2 ))")
	Collection<Article> getPrivatePublishedSuscribedArticlesByUserId(int userId, int customerId);

	@Query("select a from Article a where a.newspaper.published=1 AND (a.newspaper.publicNp=1) AND(a.title LIKE concat(concat('%',?1),'%') OR a.body LIKE concat(concat('%',?1),'%') OR a.summary LIKE concat(concat('%',?1),'%'))")
	Collection<Article> findAllValidAndPublicByKeyword(String keyword);

	// @Query("select a from Article a where (a.title LIKE concat(concat('%',?1),'%') OR a.body LIKE concat(concat('%',?1),'%') OR a.summary LIKE concat(concat('%',?1),'%')) AND a.newspaper.published=1 AND (a.newspaper.publicNp=1)")
	// Collection<Article> findAllValidAndPublicByKeyword(String keyword, int
	// id);
	// --- DASHBOARD ---

	// B1
	@Query("select 1.0*(select count(f) from FollowUp f)/count(a) from Article a")
	Double avgFollowUpsPerArticle();

	@Query("select p from Article a join a.pictureURLs p where a.id=?1")
	Collection<String> getURLsDeUnArticleId(Integer id);

	@Query("select a from Article a where (a.saved=1 and a.title LIKE concat(concat('%',?1),'%') OR a.body LIKE concat(concat('%',?1),'%') OR a.summary LIKE concat(concat('%',?1),'%')))")
	Collection<Article> findAdminByKeyword(String s);

	@Query("select a from Newspaper n join n.articles a where n.id=?1")
	Collection<Article> getArticlesOfNewspaperId(int id);

	//B2,B3
	//	@Query("select 1.0*(select count(f) from FollowUp f)/count(a) from Article a where datediff(week,a.newspaper.publicationDate,CURRENT_DATE) <= 7")
	//	Double avgFollowUpsPerArticleOneWeekAfterPublicationDate();

	// B2,B3
	// @Query("select 1.0*(select count(f) from FollowUp f)/count(a) from Article a where datediff(week,a.newspaper.publicationDate,CURRENT_DATE) <= 7")
	// Double avgFollowUpsPerArticleOneWeekAfterPublicationDate();
	
	//Paginated repository
	@Query("select a from Article a where a.creator.id=?1")
	Page<Article> getAllArticlesByUserIdPaginate(int id, Pageable p);
	
	@Query("select a from Article a where a.newspaper.published=1 AND (a.newspaper.publicNp=1)")
	Page<Article> findAllValidAndPublicPaginate(Pageable p);
	
	@Query("select a from Article a where (a.saved=1 and a.title LIKE concat(concat('%',?1),'%') OR a.body LIKE concat(concat('%',?1),'%') OR a.summary LIKE concat(concat('%',?1),'%')))")
	Page<Article> findAdminByKeywordPaginate(String s, Pageable p);
	
	@Query("select a from Article a where a.newspaper.published=1 AND (a.newspaper.publicNp=1) AND(a.title LIKE concat(concat('%',?1),'%') OR a.body LIKE concat(concat('%',?1),'%') OR a.summary LIKE concat(concat('%',?1),'%'))")
	Page<Article> findAllValidAndPublicByKeyword(String keyword, Pageable p);
	
	@Query("select a from Article a where a.newspaper.published = 1 AND (a.title LIKE concat(concat('%',?1),'%') OR a.body LIKE concat(concat('%',?1),'%') OR a.summary LIKE concat(concat('%',?1),'%')) AND  ( (a.newspaper.publicNp=1) OR (a.newspaper.publicNp= 0 AND  (a.newspaper in (select s.newspaper from Suscription s where s.customer.id =?2)))  )")
	Page<Article> findSuscriptedArticlesByKeywordPaginate(String keyword, int actorId, Pageable p);
	
//	@Query("select n from Newspaper n where n.published=1 and (n.title LIKE concat(concat('%',?1),'%') or n.description LIKE concat(concat('%',?1),'%'))")
//	Page<Article> findByKeywordPaginate(Pageable p, String keyword);

}
