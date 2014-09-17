package de.craut;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.craut.domain.Route;
import de.craut.domain.RoutePoint;
import de.craut.domain.RoutePointRepository;
import de.craut.domain.RouteRepository;

@Configuration
@Profile("hsql")
public class HsqlSetup {

	private static class InitHsqlDb implements InitDB {

		@Override
		public void init(ApplicationContext ctx) {
			RouteRepository routeRepo = ctx.getBean(RouteRepository.class);
			Route route = new Route("Karlsruhe nach Baden-Baden", new Date());
			routeRepo.save(route);
			List<RoutePoint> rpList = new ArrayList<RoutePoint>();
			RoutePoint routePoint = new RoutePoint(route, "49.022879999999986", "8.4111300000000142");
			rpList.add(routePoint);
			routePoint = new RoutePoint(route, "49.062677562713588", "8.4410415218024752");
			rpList.add(routePoint);
			routePoint = new RoutePoint(route, "49.104292209980912", "8.5084645778121342");
			rpList.add(routePoint);

			RoutePointRepository routePointRepo = ctx.getBean(RoutePointRepository.class);
			routePointRepo.save(rpList);

		}

	}

	// <LatitudeDegrees>49.062677562713588</LatitudeDegrees>
	// <LongitudeDegrees>8.4410415218024752</LongitudeDegrees>
	//
	//
	// <LatitudeDegrees>49.104292209980912</LatitudeDegrees>
	// <LongitudeDegrees>8.5084645778121342</LongitudeDegrees>

	@Bean
	public InitDB initDB() {
		return new InitHsqlDb();
	}

}
