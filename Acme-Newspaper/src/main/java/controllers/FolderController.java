
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
	public ModelAndView create() {
		ModelAndView result;
		Folder folder;

		folder = this.folderService.create();
		result = this.createEditModelAndView(folder);

		return result;
	}

	//Edit ------------------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int folderId) {
		ModelAndView result;
		Folder folder;

		folder = this.folderService.findOne(folderId);
		Assert.notNull(folder);
		result = this.createEditModelAndView(folder);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveFinal(final Folder fol, final BindingResult binding) {
		ModelAndView result;
		Folder folder;
		folder = this.folderService.reconstruct(fol, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(folder);
		else
			try {
				if (folder.getId() != 0)
					this.folderService.save(folder);
				else if (folder.getParent() == null)
					//					System.out.println(folder.getParent());
					this.folderService.createForUserRaiz(folder.getName());
				else
					this.folderService.createForUser(folder.getName(), folder.getParent().getName());
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(folder, "folder.commit.error");
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
			result = this.createEditModelAndView(folder, "folder.commit.error");
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
		final Collection<Folder> parents = this.folderService.findByUser();

		if (folder.getActor() == null) {
			actor = null;
			children = null;
			parent = null;
			messages = null;
		} else {
			actor = folder.getActor();
			children = folder.getChildren();
			parent = folder.getParent();
			messages = this.messageService.findMessageByFolder(folder.getId());
			;
		}
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

}
