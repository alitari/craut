package de.craut.web;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.craut.domain.User;

@RestController
@RequestMapping(value = "/users")
public class UserRestController {

	@RequestMapping(value = "/{user}", method = RequestMethod.GET)
	public User getUser(@PathVariable Long user) {
		return new User(user);
	}

	@RequestMapping(value = "/{user}", method = RequestMethod.DELETE)
	public User deleteUser(@PathVariable Long user) {
		return new User(user);
	}

}