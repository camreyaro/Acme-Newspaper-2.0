
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.MessageRepository;
import security.Authority;
import domain.Actor;
import domain.Folder;
import domain.Message;
import domain.Priority;
import domain.SpamWord;

@Service
@Transactional
public class MessageService {

	@Autowired
	private MessageRepository	messageRepository;
	@Autowired
	private ActorService		actorService;
	@Autowired
	private FolderService		folderService;
	@Autowired
	private SpamWordService		spamWordService;

	@Autowired
	Validator					validator;


	public MessageService() {
		super();
	}

	// CRUD methods 

	public Message create() {
		final Message message = new Message();
		Actor actor;
		actor = this.actorService.findByPrincipal();
		message.setSender(actor);
		message.setFolder(this.folderService.findFolderByActor(actor.getUserAccount().getUsername(), "inbox"));
		message.setDate(new Date(System.currentTimeMillis() - 1000));
		message.setPriority(Priority.NEUTRAL);
		message.setSpam(false);

		return message;
	}

	public Collection<Message> findAll() {
		Collection<Message> messages;

		messages = this.messageRepository.findAll();
		Assert.notNull(messages);

		return messages;
	}

	public Message findOne(final int MessageId) {
		Message message;
		message = this.messageRepository.findOne(MessageId);
		Assert.notNull(message);

		return message;
	}

	public Collection<Message> findMessageByFolder(final int folderId) {
		Collection<Message> messages;

		messages = this.messageRepository.findMessageByFolder(folderId);
		Assert.notNull(messages);

		return messages;
	}

	public Message save(final Message message) {
		Message result;
		Folder folder = null;
		String recipentUserName;
		//Esto puede petar
		final Collection<SpamWord> spamWords = this.spamWordService.findAll();
		//
		recipentUserName = message.getRecipient().getUserAccount().getUsername();
		for (final SpamWord s : spamWords)
			if (message.getBody().contains(s.getWord())) {
				message.setSpam(true);

				//El sospechso no lo necesitamos, ya que no tenemos banneo
				//final Actor a = this.actorService.suspicius();
				//message.setSender(a);
				message.setSender(this.actorService.findByPrincipal());
				break;
			} else
				message.setSpam(false);
		if (message.getSpam() == false)
			folder = this.folderService.findFolderByActor(recipentUserName, "inbox");
		/*
		 * for (final Folder m : message.getRecipient().getFolder())
		 * if (m.getName().equals("inbox")) {
		 * folder = m;
		 * break;
		 * }
		 */
		//message.setFolder(folder);
		else if (message.getSpam() == true)
			folder = this.folderService.findFolderByActor(recipentUserName, "spambox");
		//for (final Folder m : message.getRecipient().getFolder())

		/*
		 * if (m.getName().equals("spambox")) {
		 * folder = m;
		 * break;
		 * 
		 * }
		 */
		//			System.out.println("Yano puedo mas " + folder.getName());
		//message.setFolder(folder);
		System.out.println("Folder: + " + folder.getName());
		message.setFolder(folder);
		result = this.messageRepository.save(message);
		/*
		 * final Collection<Message> a = folder.getMessages();
		 * a.add(result);
		 * folder.setMessages(a);
		 */
		//final Folder res = this.folderService.save2(folder);

		Folder folderSender = null;
		String senderUserName;
		senderUserName = this.actorService.findByPrincipal().getUserAccount().getUsername();

		folderSender = this.folderService.findFolderByActor(senderUserName, "outbox");

		/*
		 * for (final Folder d : message.getSender().getFolder())
		 * if (d.getName().equals("outbox"))
		 * folderSender = d;
		 */
		message.setFolder(folderSender);
		final Message messageCopySender = this.messageRepository.save(message);
		/*
		 * final Collection<Message> b = folderSender.getMessages();
		 * b.add(result2);
		 * folderSender.setMessages(b);
		 * final Folder res2 = this.folderService.save2(folderSender);
		 */
		Assert.notNull(result);
		Assert.notNull(messageCopySender);
		return result;
	}

	public void delete(final Message message) {

		Actor actor;
		actor = this.actorService.findByPrincipal();

		final Folder folder = message.getFolder();

		final String userName = actor.getUserAccount().getUsername();
		if (folder.getName().equals("trashbox"))
			this.messageRepository.delete(message);
		else
			this.update(message, "trashbox");

	}

	public Message update(final Message message, final String folderName) {
		Actor actor;

		Message result;
		actor = this.actorService.findByPrincipal();
		final Message m = this.messageRepository.findOne(message.getId());
		final Folder folderOld = this.folderService.findFolderByActor(actor.getUserAccount().getUsername(), m.getFolder().getName());
		final Folder folderNew = this.folderService.findFolderByActor(actor.getUserAccount().getUsername(), folderName);

		message.setFolder(folderNew);
		result = this.messageRepository.save(message);

		/*
		 * folderNew.getMessages().add(message);
		 * this.folderService.save(folderNew);
		 * folderOld.getMessages().remove(message);
		 * this.folderService.save(folderOld);
		 */

		return result;
	}
	// Other bussines methods 

	public Collection<Message> sendAllUserUserMessage(final Message messageToSend) {
		final Collection<Message> m = new ArrayList<Message>();

		Actor actor;
		final Collection<Authority> a = this.actorService.findByPrincipal().getUserAccount().getAuthorities();
		Boolean r = false;
		for (final Authority x : a)
			if (x.getAuthority().equals(Authority.ADMIN))
				r = true;
		Assert.isTrue(r == true);
		actor = this.actorService.findByPrincipal();
		final Collection<Actor> all = new ArrayList<Actor>(this.actorService.findAll());

		for (final Actor x : all) {
			Message message = new Message();

			message.setSender(actor);
			message.setRecipient(x);

			message.setFolder(this.folderService.findFolderByActor(x.getUserAccount().getUsername(), "notificationbox"));
			message.setDate(new Date(System.currentTimeMillis() - 1000));
			message.setPriority(messageToSend.getPriority());
			message.setSubject(messageToSend.getSubject());
			message.setBody(messageToSend.getBody());
			message.setSpam(false);
			message = this.saveNotification(message);

			Assert.notNull(message);
			m.add(message);
		}

		final Message outboxMsg = new Message();

		outboxMsg.setSender(actor);
		outboxMsg.setRecipient(actor);

		outboxMsg.setFolder(this.folderService.findFolderByActor(actor.getUserAccount().getUsername(), "outbox"));
		outboxMsg.setDate(new Date(System.currentTimeMillis() - 1000));
		outboxMsg.setPriority(messageToSend.getPriority());
		outboxMsg.setSubject(messageToSend.getSubject());
		outboxMsg.setBody(messageToSend.getBody());
		outboxMsg.setSpam(false);

		this.messageRepository.save(outboxMsg);

		return m;

	}
	public Integer cuentaMensaje(final String t1) {
		return this.messageRepository.cuentaMensaje(t1);
	}

	public Message cogerMensajeTest(final String body, final String name) {
		return this.messageRepository.cogerMensajeTest(body, name);

	}

	public Collection<Message> getMessagesSent() {
		Collection<Message> messagesSent = new ArrayList<Message>();
		final int senderId = this.actorService.findByPrincipal().getId();
		messagesSent = this.messageRepository.getMessagesSent(senderId);

		return messagesSent;
	}

	public Collection<Message> getMessagesFromRecipient() {
		Collection<Message> messagesFromRecipient = new ArrayList<Message>();
		final int senderId = this.actorService.findByPrincipal().getId();
		messagesFromRecipient = this.messageRepository.getMessagesSent(senderId);

		return messagesFromRecipient;
	}

	public Message saveNotification(final Message message) {
		Message result;
		Folder folder = null;
		String recipentUserName;
		//Puede petar
		final Collection<SpamWord> spamWords = this.spamWordService.findAll();
		//
		recipentUserName = message.getRecipient().getUserAccount().getUsername();
		for (final SpamWord s : spamWords)
			if (message.getBody().contains(s.getWord())) {
				message.setSpam(true);

				//final Actor a = this.actorService.suspicius();
				//message.setSender(a);
				message.setSender(this.actorService.findByPrincipal());
				break;
			} else
				message.setSpam(false);
		if (message.getSpam() == false)
			folder = this.folderService.findFolderByActor(recipentUserName, "inbox");

		if (message.getSpam() == true)
			folder = this.folderService.findFolderByActor(recipentUserName, "inbox");

		message.setFolder(folder);
		result = this.messageRepository.save(message);
		//final Folder folderSender = null;
		//final String senderUserName;

		Assert.notNull(result);
		return result;
	}

	public Message reconstruct(final Message s, final BindingResult binding) {
		Message result;

		if (s.getId() == 0) {
			result = s;
			result.setSender(this.actorService.findByPrincipal());
			result.setFolder(this.folderService.findFolderByActor(this.actorService.findByPrincipal().getUserAccount().getUsername(), "inbox"));
			result.setDate(s.getDate());
			result.setPriority(s.getPriority());
			result.setSpam(false);

		} else {
			result = this.findOne(s.getId());
			Assert.notNull(result);
			result.setFolder(s.getFolder());
		}
		this.validator.validate(result, binding);

		return result;

	}

}
