package de.craut;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.craut.domain.Route;
import de.craut.domain.RouteRepository;

@Configuration
@Profile("!localmysql")
public class HsqlSetup {

	private static class InitHsqlDb implements InitDB {

		@Override
		public void init(ApplicationContext ctx) {
			RouteRepository routeRepo = ctx.getBean(RouteRepository.class);
			routeRepo.save(new Route("Grillen2", new Date()));
			routeRepo.save(new Route("Daily", new Date()));
			routeRepo.save(new Route("Estimation", new Date()));
			routeRepo.save(new Route("Retro", new Date()));
		}

	}

	@Bean
	public InitDB initDB() {
		return new InitHsqlDb();
	}

}
