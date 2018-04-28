
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Advertisement;
import domain.Newspaper;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

	@Query("select distinct a.newspaper from Advertisement a where a.agent.id=?1")
	Collection<Newspaper> findAdvertisedNewspapers(int agentId);

	@Query("select a.newspaper from Advertisement a where a.newspaper not in (select distinct a.newspaper from Advertisement a where a.agent.id = ?1)")
	Collection<Newspaper> findNotAdvertisedNewspapers(int agentId);

	@Query("select random ads from Advertisment ads where  ads.newspaper in (select a.newspaper from Article a where a.id=?1)")
	Advertisement findRandomAdvertisementByNewspaperId(int newspaperId);
}
