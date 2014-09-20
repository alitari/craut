package de.craut;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.craut.service.ActivityService;
import de.craut.service.RouteService;

@Configuration
@Profile("test")
public class TestContext {

	@Bean
	public RouteService routeService() {
		return mock(RouteService.class);
	}

	@Bean
	public ActivityService activityService() {
		return mock(ActivityService.class);
	}

}
