package de.craut;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.craut.domain.TeamEvent;
import de.craut.domain.TeamEventRepository;

@Configuration
@Profile("!localmysql")
public class HsqlSetup {

	private static class InitHsqlDb implements InitDB {

		@Override
		public void init(ApplicationContext ctx) {
			TeamEventRepository teamEventRepo = ctx.getBean(TeamEventRepository.class);
			teamEventRepo.save(new TeamEvent("Grillen2", new Date()));
			teamEventRepo.save(new TeamEvent("Daily", new Date()));
			teamEventRepo.save(new TeamEvent("Estimation", new Date()));
			teamEventRepo.save(new TeamEvent("Retro", new Date()));
		}

	}

	@Bean
	public InitDB initDB() {
		return new InitHsqlDb();
	}

}
