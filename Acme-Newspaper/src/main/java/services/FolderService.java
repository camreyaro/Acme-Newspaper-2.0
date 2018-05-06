
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FolderRepository;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Folder;

@Service
@Transactional
public class FolderService {

	@Autowired
	private FolderRepository	folderRepository;

	@Autowired
	private ActorService		actorService;

	@Autowired
	Validator					validator;


	// CRUD methods 
	//Creamos una folder, la cual tiene un actor, que es quien esta autentificado, lo cogemos con la funcion findByPrincipal de actroService
	public Folder create() {
		final Folder folder = new Folder();
		Actor actor;

		actor = this.actorService.findByPrincipal();
		folder.setActor(actor);
		//folder.setMessages(new ArrayList<Message>());
		folder.setPredefined(false);
		folder.setChildren(new ArrayList<Folder>());
		return folder;
	}

	public Folder createForUser(final String nombre) {
		Folder folder;
		Actor actor;
		Folder result;
		//final Collection<Message> messages = new ArrayList<Message>(); //Inicializamos tambien las listas aunque esten vacias
		actor = this.actorService.findByPrincipal();
		Collection<Folder> folders = new ArrayList<Folder>();
		folders = this.folderRepository.findAll();

		Assert.isTrue(!nombre.equals("inbox"));
		Assert.isTrue(!nombre.equals("outbox"));
		Assert.isTrue(!nombre.equals("notificationbox"));
		Assert.isTrue(!nombre.equals("trashbox"));
		Assert.isTrue(!nombre.equals("inspamboxbox"));

		for (final Folder f : folders)
			Assert.isTrue(!f.getName().equals(nombre));

		folder = new Folder();
		folder.setName(nombre);
		folder.setActor(actor);
		//folder.setMessages(messages);
		folder.setPredefined(true);
		folder.setChildren(new ArrayList<Folder>());

		result = this.save(folder);

		return result;
	}

	public Folder createForUser(final String nombre, final String padre) {
		Folder folder;
		Actor actor;
		Folder result;
		//final Collection<Message> messages = new ArrayList<Message>(); //Inicializamos tambien las listas aunque esten vacias
		actor = this.actorService.findByPrincipal();
		Collection<Folder> folders = new ArrayList<Folder>();
		folders = this.folderRepository.findFolderByUser(actor.getUserAccount().getId());
		final Folder parent;

		parent = this.findFolderByActor(actor.getUserAccount().getUsername(), padre);
		System.out.println("En sus muertos, aqui estoy: " + parent.getName());
		System.out.println("Nombre del padre: " + padre);

		Assert.isTrue(!nombre.equals("inbox"));
		Assert.isTrue(!nombre.equals("outbox"));
		Assert.isTrue(!nombre.equals("notificationbox"));
		Assert.isTrue(!nombre.equals("trashbox"));
		Assert.isTrue(!nombre.equals("inspamboxbox"));

		for (final Folder f : folders)
			Assert.isTrue(!f.getName().equals(nombre));

		folder = new Folder();
		folder.setName(nombre);
		folder.setActor(actor);
		//folder.setMessages(messages);
		folder.setPredefined(false);
		folder.setChildren(new ArrayList<Folder>());
		folder.setParent(parent);

		System.out.println("El parent que le ponemos: " + folder.getParent().getName());

		result = this.save(folder);
		parent.getChildren().add(result);
		this.save(parent);

		return result;
	}

	public Folder createForUserRaiz(final String nombre) {
		System.out.println("Entra al save ");

		Folder folder;
		Actor actor;
		Folder result;
		//final Collection<Message> messages = new ArrayList<Message>(); //Inicializamos tambien las listas aunque esten vacias
		actor = this.actorService.findByPrincipal();
		Collection<Folder> folders = new ArrayList<Folder>();
		folders = this.folderRepository.findAll();

		Assert.isTrue(!nombre.equals("inbox"));
		Assert.isTrue(!nombre.equals("outbox"));
		Assert.isTrue(!nombre.equals("notificationbox"));
		Assert.isTrue(!nombre.equals("trashbox"));
		Assert.isTrue(!nombre.equals("inspamboxbox"));

		for (final Folder f : folders)
			Assert.isTrue(!f.getName().equals(nombre));

		folder = new Folder();
		folder.setName(nombre);
		folder.setActor(actor);
		//folder.setMessages(messages);
		folder.setPredefined(false);
		folder.setChildren(new ArrayList<Folder>());
		System.out.println(folder);
		result = this.save(folder);
		return result;
	}
	public Folder createForUser2(final String nombre, final Actor actor) {
		Folder folder;
		Folder result;
		//final Collection<Message> messages = new ArrayList<Message>(); //Inicializamos tambien las listas aunque esten vacias

		folder = new Folder();
		folder.setName(nombre);
		folder.setActor(actor);
		//folder.setMessages(messages);
		folder.setPredefined(true);
		folder.setChildren(new ArrayList<Folder>());

		//result = this.saveSystem(folder, actor);
		result = this.folderRepository.save(folder);
		return result;
	}

	public Collection<Folder> findAll() {
		Collection<Folder> folders;

		folders = this.folderRepository.findAll();
		Assert.notNull(folders);

		return folders;
	}

	public Folder findOne(final int FolderId) {
		Folder folder;
		folder = this.folderRepository.findOne(FolderId);
		Assert.notNull(folder);

		return folder;
	}

	public Collection<Folder> findByUser() {
		Collection<Folder> folders;
		final UserAccount userAccount = LoginService.getPrincipal();
		folders = this.folderRepository.findFolderByUser(userAccount.getId());
		Assert.notNull(folders);

		return folders;
	}

	public Folder save(final Folder folder) {
		Folder result;
		final Collection<Folder> folders = this.findByUser();
		Assert.isTrue(folder.getActor().getId() == this.actorService.findByPrincipal().getId(), "message.error.notActor");
		Assert.isTrue(folder.getPredefined() == false);
		if (folder.getId() > 0)
			Assert.isTrue(folders.contains(folder));
		/*
		 * if (!folder.getParent().equals(null))
		 * Assert.isTrue(this.findByUser().contains(folder.getParent()));
		 */
		result = this.folderRepository.save(folder);

		Assert.notNull(result);
		return result;
	}
	public Folder save2(final Folder folder) {
		Folder result;
		result = this.folderRepository.save(folder);

		Assert.notNull(result);

		return result;
	}
	public Folder saveSystem(final Folder folder, final Actor actor) {
		Folder result;
		result = this.folderRepository.save(folder);
		//actor.getFolder().add(result);
		//this.actorService.save(actor);
		Assert.notNull(result);
		return result;
	}

	public Collection<Folder> saveAll(final Collection<Folder> folders) {
		final Collection<Folder> results;
		final List<Folder> l = new ArrayList<Folder>(folders);

		results = this.folderRepository.save(l);
		return results;
	}
	public void delete(final Folder folder) {
		Assert.isTrue(!folder.getPredefined(), "message.error.predefined");
		Assert.isTrue(folder.getChildren().size() == 0, "message.error.children");
		if (folder.getParent() != null)
			folder.getParent().getChildren().remove(folder);
		this.folderRepository.delete(folder);
	}

	//Solo podemos hacer update del nombre de la carpeta
	public Folder update(final Folder folder, final String nombre) {
		Folder update;
		//Actor actor;
		//actor = this.actorService.findByPrincipal();

		folder.setName(nombre);
		update = this.save(folder);

		//actor.getFolder().remove(folder);
		//actor.getFolder().add(update);

		return update;
	}

	// Other bussines methods 

	public Collection<Folder> createSystemFolders(final Actor actor) {
		final Collection<Folder> folders = new ArrayList<Folder>();
		folders.add(this.createForUser2("inbox", actor));
		folders.add(this.createForUser2("notificationbox", actor));
		folders.add(this.createForUser2("outbox", actor));
		folders.add(this.createForUser2("trashbox", actor));
		folders.add(this.createForUser2("spambox", actor));

		return folders;
	}

	public Folder findFolderByActor(final String actorName, final String folderName) {
		Folder folder;
		folder = this.folderRepository.findFolderByActor(actorName, folderName);
		return folder;
	}
	public Integer CountSystemFoldersByActor(final int id) {
		return this.folderRepository.CountSystemFoldersByActor(id);
	}

	public Folder reconstruct(final Folder s, final BindingResult binding) {
		Folder result;
		final Actor actor = this.actorService.findByPrincipal();

		if (s.getId() == 0) {
			result = s;
			result.setParent(s.getParent());
			result.setActor(actor);

		} else {
			result = this.findOne(s.getId());
			Assert.notNull(result);
			result.setName(s.getName());
		}

		System.out.println("el paarent: " + result.getParent());

		this.validator.validate(result, binding);

		return result;

	}

	public Collection<Folder> findRaizFolders() {
		final Collection<Folder> foldersRaiz = this.folderRepository.findRaizFolders(this.actorService.findByPrincipal().getUserAccount().getId());
		return foldersRaiz;
	}
}
