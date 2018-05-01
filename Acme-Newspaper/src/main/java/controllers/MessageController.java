
package controllers;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.FolderService;
import services.MessageService;
import domain.Actor;
import domain.Folder;
import domain.Message;
import domain.Priority;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

	//Services ---------------------------------------------------

	@Autowired
	MessageService	messageService;

	@Autowired
	FolderService	folderService;

	@Autowired
	ActorService	actorService;


	// Constructors -----------------------------------------------

	public MessageController() {
		super();
	}

	//Listing ------------------------------------------------------

	/*
	 * @RequestMapping(value = "/list", method = RequestMethod.GET)
	 * public ModelAndView list(@RequestParam final int folderId) {
	 * ModelAndView result;
	 * final Collection<Folder> foldersUser = this.actorService.findByPrincipal().getFolder();
	 * final Folder folder = this.folderService.findOne(folderId);
	 * Boolean out = false;
	 * for (final Folder fo : foldersUser)
	 * if (fo.getId() == folderId) {
	 * out = true;
	 * break;
	 * }
	 * if (out == true) {
	 * final Collection<Message> messages = this.messageService.findMessageByFolder(folderId);
	 * result = new ModelAndView("message/list");
	 * result.addObject("messages", messages);
	 * result.addObject("folder", folder);
	 * result.addObject("requestURI", "message/list.do");
	 * } else {
	 * //Para evitar el panic, redireccinamos a outbox del user
	 * final Folder f = this.folderService.findFolderByActor(this.actorService.findByPrincipal().getName(), "outbox");
	 * final Collection<Message> messages2 = this.messageService.findMessageByFolder(f.getId());
	 * result = new ModelAndView("message/list");
	 * result.addObject("messages", messages2);
	 * result.addObject("folder", f);
	 * result.addObject("requestURI", "message/list.do");
	 * }
	 * return result;
	 * 
	 * }
	 */

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int folderId) {
		ModelAndView result;
		//final Collection<Folder> foldersUser = this.actorService.findByPrincipal().getFolder();
		final Collection<Folder> foldersUser = this.folderService.findByUser();
		Boolean out = false;

		for (final Folder fol : foldersUser)
			if (fol.getId() == folderId)
				out = true;
		if (out = false) {
			final Folder f = this.folderService.findFolderByActor(this.actorService.findByPrincipal().getUserAccount().getUsername(), "outbox");
			final Collection<Message> messages2 = this.messageService.findMessageByFolder(f.getId());
			final Collection<Folder> foldersChil = this.folderService.findByUser();
			final Collection<Folder> childrens = new ArrayList<Folder>();

			for (final Folder f1 : foldersChil)
				if (f1.getParent() == f)
					childrens.add(f1);

			result = new ModelAndView("message/list");
			result.addObject("messages", messages2);
			result.addObject("folder", f);
			result.addObject("childrens", childrens);
			result.addObject("requestURI", "message/list.do");
		} else {
			final Folder folder = this.folderService.findOne(folderId);
			final Collection<Message> messages2 = this.messageService.findMessageByFolder(folderId);
			final Collection<Folder> foldersChil2 = this.folderService.findByUser();
			final Collection<Folder> childrens2 = new ArrayList<Folder>();

			for (final Folder f1 : foldersChil2)
				if (f1.getParent() == folder)
					childrens2.add(f1);

			result = new ModelAndView("message/list");
			result.addObject("messages", messages2);
			result.addObject("childrens", childrens2);
			result.addObject("folder", folder);
			result.addObject("requestURI", "message/list.do");
		}
		return result;

	}

	//Display ------------------------------------------------------

	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int messageId) {
		ModelAndView result;
		final Message message = this.messageService.findOne(messageId);
		final Actor sender = message.getSender();
		final Actor recipient = message.getRecipient();

		result = new ModelAndView("message/display");
		result.addObject("messages", message);
		result.addObject("sender", sender);
		result.addObject("recipient", recipient);

		return result;

	}

	//Create ------------------------------------
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int all) {
		ModelAndView result = null;
		Message message;
		final String admin = "ADMIN";
		final Collection<Authority> au = this.actorService.findByPrincipal().getUserAccount().getAuthorities();
		message = this.messageService.create();
		System.out.println("Mensaje creado folder: " + message.getFolder());

		boolean a = false;
		for (final Authority au2 : au)
			if (au2.getAuthority().equals(admin))
				a = true;

		if (a == true)
			if (all == 1)
				result = this.AllModelAndView(message);
			else
				result = this.createEditModelAndView(message);
		if (a == false)
			if (all == 1)
				result = new ModelAndView("redirect:/message/create.do?all=0");
			else
				result = this.createEditModelAndView(message);
		return result;
	}

	//Edit ------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int messageId) {
		ModelAndView result = null;
		Message message;

		message = this.messageService.findOne(messageId);
		Assert.notNull(message);
		result = this.createEditModelAndView(message);

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Message mes, final BindingResult binding, final String priority) {
		ModelAndView result;
		Message message;
		if (mes.getId() == 0) {
			Priority prioridad;
			if (priority.equals("LOW"))
				prioridad = Priority.LOW;
			else if (priority.equals("NEUTRAL"))
				prioridad = Priority.NEUTRAL;
			else
				prioridad = Priority.HIGH;
			mes.setPriority(prioridad);
		}

		message = this.messageService.reconstruct(mes, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(message);
		else
			try {
				Assert.notNull(message.getRecipient());
				if (message.getId() != 0)
					this.messageService.update(message, message.getFolder().getName());
				else
					this.messageService.save(message);
				final int folderId = message.getFolder().getId();
				result = new ModelAndView("redirect:list.do?folderId=" + folderId);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(message, "message.commit.error");
			}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveAll")
	public ModelAndView saveAll(final Message mes, final BindingResult binding, final String priority) {
		ModelAndView result;
		Priority prioridad;
		Message message;
		if (priority.equals("LOW"))
			prioridad = Priority.LOW;
		else if (priority.equals("NEUTRAL"))
			prioridad = Priority.NEUTRAL;
		else
			prioridad = Priority.HIGH;
		//Esto estaba dentro del try, si peta, podria ser por eso
		mes.setPriority(prioridad);
		message = this.messageService.reconstruct(mes, binding);
		//
		if (binding.hasErrors())
			result = this.AllModelAndView(message);
		else
			try {
				this.messageService.notificationMail(message);

				final int folderId = this.folderService.findFolderByActor(this.actorService.findByPrincipal().getUserAccount().getUsername(), "outbox").getId();

				result = new ModelAndView("redirect:list.do?folderId=" + folderId);
			} catch (final Throwable oops) {
				System.out.println(oops.getMessage());
				result = this.AllModelAndView(message, "message.commit.error");
			}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Message message, final BindingResult binding) {
		ModelAndView result;
		Message messageR;

		messageR = this.messageService.reconstruct(message, binding);

		try {
			this.messageService.delete(messageR);
			final int folderId = messageR.getFolder().getId();
			result = new ModelAndView("redirect:list.do?folderId=" + folderId);
		} catch (final Throwable oops) {
			System.out.println(oops.getMessage());
			result = this.createEditModelAndView(messageR, "message.commit.error");
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Message message) {
		ModelAndView result;

		result = this.createEditModelAndView(message, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Message message, final String messageCode) {
		final ModelAndView result;
		final Actor sender;
		final Actor recipient;
		final Folder folder;
		final Collection<Folder> folders = this.folderService.findByUser();
		final Collection<Actor> recipients = this.actorService.findAll();
		final Collection<Priority> priorities = new ArrayList<Priority>();
		final String all = "0";

		priorities.add(Priority.LOW);
		priorities.add(Priority.NEUTRAL);
		priorities.add(Priority.HIGH);

		if (message.getSender() == null) {
			sender = null;
			recipient = null;
			folder = null;

		} else {
			sender = message.getSender();
			recipient = message.getRecipient();
			folder = message.getFolder();
		}
		result = new ModelAndView("message/edit");
		result.addObject("message", message);
		result.addObject("sender", sender);
		result.addObject("recipient", recipient);
		result.addObject("recipients", recipients);
		result.addObject("folder", folder);
		result.addObject("folders", folders);
		result.addObject("all", all);
		result.addObject("priority", priorities);
		result.addObject("requestURI", "message/create.do?all=0");

		result.addObject("othermessage", messageCode);

		return result;
	}

	//------------------------------------------------
	protected ModelAndView AllModelAndView(final Message message) {
		ModelAndView result;

		result = this.AllModelAndView(message, null);

		return result;
	}

	protected ModelAndView AllModelAndView(final Message message, final String messageCode) {

		final ModelAndView result;
		final Actor sender;
		final Actor recipient;
		final Folder folder;
		final String all = "1";
		final Collection<Folder> folders = this.folderService.findByUser();
		final Collection<Actor> recipients = this.actorService.findAll();

		if (message.getSender() == null) {
			sender = null;
			recipient = null;
			folder = null;

		} else {
			sender = message.getSender();
			recipient = message.getRecipient();
			folder = message.getFolder();
		}
		result = new ModelAndView("message/edit");
		result.addObject("message", message);
		result.addObject("sender", sender);
		result.addObject("recipient", recipient);
		result.addObject("recipients", recipients);
		result.addObject("folder", folder);
		result.addObject("folders", folders);
		result.addObject("all", all);
		result.addObject("requestURI", "message/create.do?all=1");

		result.addObject("othermessage", messageCode);

		return result;
	}

}
