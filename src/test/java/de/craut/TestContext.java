package de.craut;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.craut.service.TeamEventService;

@Configuration
@Profile("test")
public class TestContext {

	@Bean
	public TeamEventService teamEventService() {
		return mock(TeamEventService.class);
	}

}
