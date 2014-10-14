package de.craut.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.craut.domain.User;

@Controller
@EnableAutoConfiguration
public class LoginController extends AbstractController {

	@RequestMapping("/login")
	public String login(Model model) {
		fillPageContent(model, "login");
		return "login";
	}

	@RequestMapping("/logout")
	public String logout(Model model) {
		userService.logout();
		return "redirect:login";
	}

	@RequestMapping("/login/create")
	public String create(@RequestParam(value = "username", required = true) String name, Model model) {
		fillPageContent(model, "login");
		if (StringUtils.isEmpty(name)) {
			model.addAttribute("message", "Enter a username!");
		} else {
			User user = userService.create(name);
			if (user != null) {
				model.addAttribute("username", user.getName());
				model.addAttribute("password", user.getPassword());
			} else {
				model.addAttribute("message", "username \'" + name + "\' already exists, please choose another.");
			}
		}

		return "login";
	}

}