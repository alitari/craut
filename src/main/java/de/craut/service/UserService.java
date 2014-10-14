package de.craut.service;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.craut.domain.User;
import de.craut.domain.UserRepository;

@Service
public class UserService implements UserDetailsService {

	private UserRepository userReopository;

	@Autowired
	private User user;

	@Autowired
	public UserService(UserRepository repo) {
		this.userReopository = repo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		user = userReopository.findByName(username);
		if (user == null) {
			return null;
		}
		List<GrantedAuthority> auth = setupAuthorisation(user);
		org.springframework.security.core.userdetails.User userDetails = setupAuthenticationContext(username, auth, user.getPassword());

		return userDetails;
	}

	private List<GrantedAuthority> setupAuthorisation(User user) {
		List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
		if (user.getName().equals("admin")) {
			auth = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN");
		}
		return auth;
	}

	private org.springframework.security.core.userdetails.User setupAuthenticationContext(String username, List<GrantedAuthority> auth, String password) {
		org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(username, password, auth);

		final RememberMeAuthenticationToken rememberMeAuthenticationToken = new RememberMeAuthenticationToken("magicHashkey", userDetails, null);
		rememberMeAuthenticationToken.setAuthenticated(true);
		SecurityContextHolder.getContext().setAuthentication(rememberMeAuthenticationToken);
		return userDetails;
	}

	public User create(String name) {
		if (userReopository.findByName(name) != null) {
			return null;
		}

		String password = RandomStringUtils.randomAlphabetic(4);
		user = userReopository.save(new User(name, password));
		return user;
	}

	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	// public User getLoggedUser() {
	// User loggedUser = null;
	// UserDetails userDetails = getLoggedUserDetails();
	// if (userDetails != null) {
	// loggedUser = userReopository.findByName(userDetails.getUsername());
	// }
	// return loggedUser;
	// }

	public UserDetails getLoggedUserDetails() {
		UserDetails loggedUserDetails = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthenticated(authentication)) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetails) {
				loggedUserDetails = ((UserDetails) principal);
			} else {
				throw new RuntimeException("Expected class of authentication principal is AuthenticationUserDetails. Given: " + principal.getClass());
			}
		}
		return loggedUserDetails;
	}

	private boolean isAuthenticated(Authentication authentication) {
		return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
	}

}