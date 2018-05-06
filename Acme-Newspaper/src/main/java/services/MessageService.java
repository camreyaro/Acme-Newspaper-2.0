
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
		message.setRecipient(actor);
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
				message.setSender(this.actorService.findByPrincipal());
				break;
			} else
				message.setSpam(false);
		if (message.getSpam() == false)
			folder = this.folderService.findFolderByActor(recipentUserName, "inbox");

		else if (message.getSpam() == true)
			folder = this.folderService.findFolderByActor(recipentUserName, "spambox");

		message.setFolder(folder);
		result = this.messageRepository.save(message);

		Folder folderSender = null;
		String senderUserName;
		senderUserName = this.actorService.findByPrincipal().getUserAccount().getUsername();

		folderSender = this.folderService.findFolderByActor(senderUserName, "outbox");

		message.setFolder(folderSender);
		final Message messageCopySender = this.messageRepository.save(message);

		Assert.notNull(result);
		Assert.notNull(messageCopySender);
		return result;
	}

	public void delete(final Message message) {

		final Folder folder = message.getFolder();

		if (folder.getName().equals("trashbox")) {
			System.out.println("Entra al delete");
			this.deleteRepo(message);

		} else
			this.update(message, "trashbox");

	}
	public void deleteRepo(final Message message) {
		this.messageRepository.delete(message);
	}

	public Message update(final Message message, final String folderName) {
		Actor actor;

		Message result;
		actor = this.actorService.findByPrincipal();
		final Message m = this.messageRepository.findOne(message.getId());
		final Folder folderNew = this.folderService.findFolderByActor(actor.getUserAccount().getUsername(), folderName);
		Assert.isTrue(folderNew.getActor().getId() == this.actorService.findByPrincipal().getId(), "message.error.notActor");
		Assert.isTrue(m.getRecipient().getId() == this.actorService.findByPrincipal().getId(), "message.error.notActor");

		message.setFolder(folderNew);

		result = this.saveSave(message);

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

		System.out.println("Empezamos a mandar mensajes a todos");
		for (final Actor x : all) {
			final Message message = new Message();
			Message result;

			message.setSender(actor);
			message.setRecipient(x);

			message.setFolder(this.folderService.findFolderByActor(x.getUserAccount().getUsername(), "notificationbox"));
			message.setDate(new Date(System.currentTimeMillis() - 1000));
			message.setPriority(messageToSend.getPriority());
			message.setSubject(messageToSend.getSubject());
			message.setBody(messageToSend.getBody());
			message.setSpam(false);
			System.out.println("vamos a hacer el save");

			result = this.saveNotification(message);

			Assert.notNull(result);
			m.add(result);
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

		this.saveNotification(outboxMsg);

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
		final Collection<SpamWord> spamWords = this.spamWordService.findAll();
		//
		recipentUserName = message.getRecipient().getUserAccount().getUsername();
		for (final SpamWord s : spamWords)
			if (message.getBody().contains(s.getWord())) {
				message.setSpam(true);
				message.setSender(this.actorService.findByPrincipal());
				break;
			} else
				message.setSpam(false);
		if (message.getSpam() == false)
			folder = this.folderService.findFolderByActor(recipentUserName, "inbox");

		else if (message.getSpam() == true)
			folder = this.folderService.findFolderByActor(recipentUserName, "spambox");

		message.setFolder(folder);
		result = this.messageRepository.save(message);

		Assert.notNull(result);
		return result;
	}

	public Message reconstruct(final Message s, final BindingResult binding) {
		Message result;
		if (s.getId() == 0) {
			result = s;
			result.setSender(this.actorService.findByPrincipal());
			result.setFolder(this.folderService.findFolderByActor(this.actorService.findByPrincipal().getUserAccount().getUsername(), "inbox"));
			result.setDate(new Date(System.currentTimeMillis() - 1000));
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

	public Collection<Message> notificationMail(final Message message) {

		Message result;
		Folder folder;
		final Collection<Message> menRes = new ArrayList<Message>();

		final Collection<Actor> receptores = this.actorService.findAll();

		final Collection<SpamWord> spamWords = this.spamWordService.findAll();
		message.setSpam(false);
		for (final SpamWord s : spamWords)
			if (message.getBody().contains(s.getWord()))
				message.setSpam(true);
		//Mandamos el correo para cada receptor
		for (final Actor a : receptores) {
			message.setRecipient(a);
			if (message.getSpam() == true) {
				folder = this.folderService.findFolderByActor(message.getRecipient().getUserAccount().getUsername(), "spambox");
				message.setFolder(folder);
			} else {
				folder = this.folderService.findFolderByActor(message.getRecipient().getUserAccount().getUsername(), "notificationbox");
				message.setFolder(folder);
			}
			if (a.getUserAccount().getId() == message.getSender().getUserAccount().getId()) {//Si el actor es el mismo, guardamos el mensaje en outbox
				final Folder folderSender = this.folderService.findFolderByActor(message.getSender().getUserAccount().getUsername(), "outbox");
				message.setFolder(folderSender);
				result = this.saveSave(message);

			} else {
				result = this.saveSave(message);
				Assert.notNull(result);
				menRes.add(result);
			}

		}
		return menRes;
	}

	public Message saveSave(final Message message) {
		Message result;
		result = this.messageRepository.save(message);
		return result;
	}

}
