package de.craut;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.craut.domain.User;
import de.craut.service.ActivityService;
import de.craut.service.ChallengeService;
import de.craut.service.RouteService;
import de.craut.service.UserService;

@Configuration
@Profile("test")
public class TestContext {

	public TestContext() {
		super();
	}

	@Bean
	public RouteService routeService() {
		return mock(RouteService.class);
	}

	@Bean
	public ActivityService activityService() {
		return mock(ActivityService.class);
	}

	@Bean
	public UserService userService() {
		return mock(UserService.class);
	}

	@Bean
	public ChallengeService challengeService() {
		return mock(ChallengeService.class);
	}

	@Bean
	public User user() {
		return mock(User.class);
	}

}
