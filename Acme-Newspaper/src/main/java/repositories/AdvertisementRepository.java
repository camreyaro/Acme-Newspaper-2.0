
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
	
	@Query("select a from Advertisement a join a.newspaper n where n.id=?1")
	Collection<Advertisement> getAdvertisementsByNewspaperId(int newspaperId);

	//Dashboard

	//Si hay 10 newspapers, 3 con publi, 7 sin publi, sería ¿3/7 ó 3/10?
	@Query("select 1.0*(" + "select count(n) from Newspaper n where n in (select a.newspaper from Advertisement a))" + "/" + "count(n2) from Newspaper n2 where n2 not in (select a.newspaper from Advertisement a)")
	Double ratioNewspaperWithAdsVsWithoutAds();

	//Calculamos todos los advertisements, en el servicio cogemos lo que tienen spamwords con el método getAdvertisementsWithSpamWords().size() que soporta regexp.
	@Query("select 1.0*count(a) from Advertisement a")
	Double rationAdsWithSpamwords();

}
