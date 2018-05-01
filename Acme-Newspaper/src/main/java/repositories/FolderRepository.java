
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Integer> {

	@Query("select f from Folder f where f.actor.userAccount.username =?1 and f.name=?2")
	Folder findFolderByActor(String username, String folderName);

	@Query("select count(f) from Folder f where f.actor.id=?1 AND f.predefined=1")
	Integer CountSystemFoldersByActor(int idActor);

	@Query("select f from Folder f where f.actor.userAccount.id =?1")
	Collection<Folder> findFolderByUser(int userId);

	@Query("select f from Folder f where f.actor.userAccount.id =?1 AND f.parent = null")
	Collection<Folder> findRaizFolders(int userId);

}
