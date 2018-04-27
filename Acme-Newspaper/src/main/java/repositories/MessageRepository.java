
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

	@Query("select count(m) from Message m where m.body=?1")
	Integer cuentaMensaje(String t1);

	@Query("select m from Message m where m.body=?1 AND m.sender.name = ?2")
	Message cogerMensajeTest(String t1, String usserAccountId);

	@Query("select m from Message m where m.folder.id=?1 ")
	Collection<Message> findMessageByFolder(int folderId);

	@Query("select m from Message m where m.sender.id=?1 ")
	Collection<Message> getMessagesSent(int senderId);

	@Query("select m from Message m where m.recipient.id=?1 ")
	Collection<Message> getMessagesFromRecipient(int recipientId);

}
