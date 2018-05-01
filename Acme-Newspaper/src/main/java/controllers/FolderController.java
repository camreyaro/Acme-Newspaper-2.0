
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

import services.ActorService;
import services.FolderService;
import services.MessageService;
import domain.Actor;
import domain.Folder;
import domain.Message;

@Controller
@RequestMapping("/folder")
public class FolderController extends AbstractController {

	//Services ---------------------------------------------------

	@Autowired
	FolderService	folderService;

	@Autowired
	ActorService	actorService;

	@Autowired
	MessageService	messageService;


	// Constructors -----------------------------------------------

	public FolderController() {
		super();
	}

	//Listing ------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		final Collection<Folder> res = new ArrayList<Folder>();
		final Collection<Folder> folders = this.folderService.findByUser();
		for (final Folder f : folders)
			if (f.getParent() == null)
				res.add(f);

		result = new ModelAndView("folder/list");
		result.addObject("folders", res);
		result.addObject("requestURI", "folder/list.do");

		return result;

	}
	//Create ---------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int folderId) {
		ModelAndView result;
		Folder folder;
		folder = this.folderService.create();

		if (folderId == 0)
			result = this.createEditModelAndView(folder);
		else {
			System.out.println("Entramos aqui");
			result = this.createEditModelAndViewList(folder, folderId);
		}
		return result;
	}

	//Edit ------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int folderId) {
		ModelAndView result;
		Folder folder;

		folder = this.folderService.findOne(folderId);
		System.out.println("soy el /edit: " + folder.getParent());
		Assert.notNull(folder);
		result = this.createEditModelAndView(folder);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveFinal(final Folder fol, final BindingResult binding) {
		ModelAndView result;
		Folder folder;
		folder = this.folderService.reconstruct(fol, binding);
		System.out.println("id: " + folder.getId());
		System.out.println("id2: " + fol.getId());
		System.out.println("Aquiiiii: " + folder.getParent().getName());
		if (!folder.getActor().equals(this.actorService.findByPrincipal()))
			result = new ModelAndView("redirect:list.do");
		else if (binding.hasErrors())
			result = this.createEditModelAndView(folder);
		else
			try {
				if (folder.getId() != 0) {
					folder.setParent(this.folderService.findOne(folder.getId()).getParent());
					this.folderService.save(folder);
				} else if (folder.getParent().getId() > 0) {
					this.folderService.createForUser(folder.getName(), folder.getParent().getName());
					System.out.println("Estoy en la shit");
				} else if (folder.getParent() == null)
					this.folderService.createForUserRaiz(folder.getName());
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				System.out.println(oops.getMessage());
				String errorMessage = "folder.commit.error";
				if (oops.getMessage().contains("message.error"))
					errorMessage = oops.getMessage();
				result = this.createEditModelAndView(folder, errorMessage);
			}
		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Folder folder, final BindingResult binding) {
		ModelAndView result;

		try {
			this.folderService.delete(folder);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			String errorMessage = "folder.commit.error";
			if (oops.getMessage().contains("message.error"))
				errorMessage = oops.getMessage();
			result = this.createEditModelAndView(folder, errorMessage);
			System.out.println("Error: " + oops.getMessage());
		}
		return result;
	}

	protected ModelAndView createEditModelAndView(final Folder folder) {
		ModelAndView result;

		result = this.createEditModelAndView(folder, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Folder folder, final String messageCode) {
		final ModelAndView result;
		Actor actor;
		Collection<Folder> children;
		Folder parent;
		Collection<Message> messages;
		final Collection<Folder> parents = this.folderService.findRaizFolders();

		actor = folder.getActor();
		children = folder.getChildren();
		parent = folder.getParent();
		messages = this.messageService.findMessageByFolder(folder.getId());

		folder.setParent(parent);
		result = new ModelAndView("folder/edit");
		result.addObject("folder", folder);
		result.addObject("actor", actor);
		result.addObject("children", children);
		result.addObject("parent", parent);
		result.addObject("messages", messages);
		result.addObject("parents", parents);

		result.addObject("message", messageCode);

		return result;
	}
	protected ModelAndView createEditModelAndViewList(final Folder folder, final int folderId) {
		ModelAndView result;

		result = this.createEditModelAndViewList(folder, null, folderId);

		return result;
	}

	protected ModelAndView createEditModelAndViewList(final Folder folder, final String messageCode, final int folderId) {
		final ModelAndView result;
		Actor actor;
		Collection<Folder> children;
		Folder parent;
		Collection<Message> messages;
		final Collection<Folder> parents = this.folderService.findRaizFolders();

		actor = this.actorService.findByPrincipal();
		children = folder.getChildren();
		parent = this.folderService.findOne(folderId);
		messages = this.messageService.findMessageByFolder(folder.getId());

		folder.setParent(this.folderService.findOne(folderId));
		System.out.println("Parent de la carpeta: " + folder.getParent());

		result = new ModelAndView("folder/edit");
		result.addObject("folder", folder);
		result.addObject("actor", actor);
		result.addObject("children", children);
		result.addObject("messages", messages);
		result.addObject("parents", parents);

		result.addObject("message", messageCode);

		return result;
	}

}
