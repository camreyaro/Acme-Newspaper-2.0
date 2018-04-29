
package repositories;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Advertisement;
import domain.Newspaper;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

	@Query("select a from Advertisement a where a.agent.id=?1")
	Collection<Advertisement> findAdvertisementsByAgent(int agentId);

	@Query("select distinct a.newspaper from Advertisement a where a.agent.id=?1")
	Collection<Newspaper> findAdvertisedNewspapers(int agentId);

	@Query("select n from Newspaper n where n.published=1 and n not in (select distinct a.newspaper from Advertisement a where a.agent.id = ?1)")
	Collection<Newspaper> findNotAdvertisedNewspapers(int agentId);

	@Query("select ads from Advertisement ads where  ads.newspaper in (select a.newspaper from Article a where a.newspaper.id=?1) order by rand()")
	ArrayList<Advertisement> findRandomAdvertisementByNewspaperId(int newspaperId);
}
