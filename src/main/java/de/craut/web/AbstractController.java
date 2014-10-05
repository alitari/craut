package de.craut.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import de.craut.domain.FileUpload;
import de.craut.service.RouteService;

public class AbstractController {

	@Autowired
	protected RouteService routeService;

	List<NavElement> createMainMenu(final String current) {

		List<NavElement> mainMenu = new ArrayList<NavElement>();
		mainMenu.add(new NavElement("/activities/list", "Activities"));
		mainMenu.add(new NavElement("/routes/list", "Routes"));
		mainMenu.add(new NavElement("/challanges/list", "Challenges"));
		CollectionUtils.forAllDo(mainMenu, new Closure() {
			@Override
			public void execute(Object input) {
				NavElement navElement = (NavElement) input;
				navElement.setSelected(navElement.getHref().contains(current));
			}
		});
		return mainMenu;

	}

	private Object createFilterMenu(String page) {
		List<NavElement> filterMenu = new ArrayList<NavElement>();
		filterMenu.add(new NavElement("/Help", "Help", false));
		filterMenu.add(new NavElement("/Contribute", "Contribute", false));
		filterMenu.add(new NavElement("/User/details", "User", true));
		return filterMenu;
	}

	protected void fillPageContent(Model model, String page) {
		model.addAttribute("menuMain", createMainMenu(page));
		model.addAttribute("menuHeader", createFilterMenu(page));

		List<FileUpload> uploadsFromToday = routeService.fetchFileUploadsFromToday();
		model.addAttribute("uploadsFromToday", uploadsFromToday);

	}

	protected FileUpload uploadFile(MultipartFile file, Model model, FileUpload.Type type) {
		FileUpload fileUpload = null;
		String uploadMessage = "no message";
		if (!file.isEmpty()) {
			byte[] content = null;
			try {
				content = file.getBytes();
			} catch (IOException e) {
				uploadMessage = e.getClass().getSimpleName() + " " + e.getMessage();
			}
			fileUpload = routeService.fileUpload(content, type, FileUpload.Format.GPX);
			uploadMessage = "File successfully uploaded! (" + content.length + " Bytes)";

		} else {
			uploadMessage = "File is empty!";
		}
		model.addAttribute("uploadMessage", (fileUpload == null ? "Error:" : "") + uploadMessage);
		return fileUpload;
	}

}