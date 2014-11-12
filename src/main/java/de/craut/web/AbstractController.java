package de.craut.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Closure;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import de.craut.domain.FileUpload;
import de.craut.domain.User;
import de.craut.service.RouteService;
import de.craut.service.UserService;
import de.craut.util.geocalc.GPXParser;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;

public class AbstractController {

	@Autowired
	protected RouteService routeService;

	@Autowired
	protected User user;

	@Autowired
	protected UserService userService;

	List<NavElement> createMainMenu(final String current) {

		List<NavElement> mainMenu = new ArrayList<NavElement>();
		mainMenu.add(new NavElement("/challenges/list", "Challenges"));
		mainMenu.add(new NavElement("/activities/list", "Activities"));
		mainMenu.add(new NavElement("/routes/list", "Routes"));
		CollectionUtils.forAllDo(mainMenu, new Closure() {
			@Override
			public void execute(Object input) {
				NavElement navElement = (NavElement) input;
				navElement.setSelected(navElement.getText().equalsIgnoreCase(current));
			}
		});
		return mainMenu;

	}

	private Object createHeaderMenu(String page, Model model) {
		List<NavElement> filterMenu = new ArrayList<NavElement>();
		filterMenu.add(new NavElement("/Help", "Help", false));
		filterMenu.add(new NavElement("/Contribute", "Contribute", false));
		if (user != null && user.getName() != null) {
			filterMenu.add(new NavElement("/users/?id=" + user.getId(), user.getName(), true));
			filterMenu.add(new NavElement("/logout", "Logout", false));
		}
		return filterMenu;
	}

	protected void fillPageContent(Model model, String entities) {
		fillPageContent(model, entities, null);
	}

	protected void fillPageContent(Model model, String entities, Page<?> listPage) {
		updateUserData();
		model.addAttribute("menuMain", createMainMenu(entities));
		model.addAttribute("menuHeader", createHeaderMenu(entities, model));

		if (listPage != null) {
			int current = listPage.getNumber();
			int total = listPage.getTotalPages();
			model.addAttribute("currentIndex", current);
			model.addAttribute("totalIndex", total);
			model.addAttribute(entities, listPage.getContent());
		}

	}

	protected void updateUserData() {
		if (user != null) {
			UserDetails loggedUserDetails = userService.getLoggedUserDetails();
			if (loggedUserDetails != null) {
				user.setName(loggedUserDetails.getUsername());
			}
		}
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

	protected List<GpxTrackPoint> parseGpxFile(MultipartFile file) {
		InputStream inputStream = null;
		try {
			inputStream = file.getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		GPXParser gpxParser = new GPXParser();
		List<GpxTrackPoint> trkPoints = gpxParser.parse(inputStream);
		return trkPoints;
	}

	@ModelAttribute("user")
	private User getUser() {
		return user;
	}

}