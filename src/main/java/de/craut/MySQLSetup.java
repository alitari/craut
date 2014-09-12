package de.craut;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("localmysql")
public class MySQLSetup {

	@Bean
	public InitDB initDB() {
		return new InitDB() {

			@Override
			public void init(ApplicationContext ctx) {
				//
			}
		};
	}

}
