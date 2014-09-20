package de.craut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.craut.domain.ActivityPoint;
import de.craut.domain.ActivityPointRepository;
import de.craut.domain.ActivityRepository;
import de.craut.domain.Route;
import de.craut.domain.Activity;
import de.craut.domain.RoutePoint;
import de.craut.domain.RoutePointRepository;
import de.craut.domain.RouteRepository;

@Configuration
@Profile("hsql")
public class HsqlSetup {

	private static class InitHsqlDb implements InitDB {

		@Override
		public void init(ApplicationContext ctx) {
			RouteRepository routeRepository = ctx.getBean(RouteRepository.class);
			Route route = new Route("Baden-Baden", new Date());
			routeRepository.save(route);

			RoutePoint[] rpoints = createRoutePoints(route);

			List<RoutePoint> rpList = Arrays.asList(rpoints);

			List<ActivityPoint> apList = new ArrayList<ActivityPoint>();

			Date start = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
			ActivityRepository activityRepository = ctx.getBean(ActivityRepository.class);
			Activity activity = new Activity("Baden Baden Act", route, start);
			activityRepository.save(activity);

			for (int i = 0; i < rpList.size(); i++) {
				RoutePoint routePoint = rpList.get(i);
				routePoint.setSequence(i);
				ActivityPoint activityPoint = new ActivityPoint(activity, routePoint, start);
				start = DateUtils.addSeconds(start, 1);
			}

			RoutePointRepository routePointRepo = ctx.getBean(RoutePointRepository.class);
			routePointRepo.save(rpList);

			ActivityPointRepository activityPointRepo = ctx.getBean(ActivityPointRepository.class);
			activityPointRepo.save(apList);

		}

		private RoutePoint[] createRoutePoints(Route route) {
			RoutePoint[] rpoints = new RoutePoint[] { new RoutePoint(route, 0, 48.783405, 8.20354), new RoutePoint(route, 0, 48.783775, 8.202754),
			        new RoutePoint(route, 0, 48.784084, 8.202045), new RoutePoint(route, 0, 48.784477, 8.201142),
			        new RoutePoint(route, 0, 48.784767, 8.200213), new RoutePoint(route, 0, 48.785069, 8.1992), new RoutePoint(route, 0, 48.785572, 8.198301),
			        new RoutePoint(route, 0, 48.786045, 8.197291), new RoutePoint(route, 0, 48.786533, 8.196348),
			        new RoutePoint(route, 0, 48.787243, 8.195672), new RoutePoint(route, 0, 48.788071, 8.195345),
			        new RoutePoint(route, 0, 48.788898, 8.195307), new RoutePoint(route, 0, 48.78957, 8.194707), new RoutePoint(route, 0, 48.790154, 8.194908),
			        new RoutePoint(route, 0, 48.79073, 8.195398), new RoutePoint(route, 0, 48.79126, 8.196105), new RoutePoint(route, 0, 48.791836, 8.196837),
			        new RoutePoint(route, 0, 48.792477, 8.197448), new RoutePoint(route, 0, 48.793125, 8.198131), new RoutePoint(route, 0, 48.793804, 8.19834),
			        new RoutePoint(route, 0, 48.794422, 8.199176), new RoutePoint(route, 0, 48.795067, 8.199977),
			        new RoutePoint(route, 0, 48.795124, 8.201135), new RoutePoint(route, 0, 48.795509, 8.202103),
			        new RoutePoint(route, 0, 48.796108, 8.202952), new RoutePoint(route, 0, 48.796749, 8.203724),
			        new RoutePoint(route, 0, 48.797241, 8.204524), new RoutePoint(route, 0, 48.797958, 8.20512), new RoutePoint(route, 0, 48.798645, 8.205972),
			        new RoutePoint(route, 0, 48.799217, 8.206955), new RoutePoint(route, 0, 48.799641, 8.208044),
			        new RoutePoint(route, 0, 48.799957, 8.209209), new RoutePoint(route, 0, 48.800255, 8.210369),
			        new RoutePoint(route, 0, 48.800564, 8.211535), new RoutePoint(route, 0, 48.800938, 8.212642),
			        new RoutePoint(route, 0, 48.801388, 8.213676), new RoutePoint(route, 0, 48.801929, 8.214728),
			        new RoutePoint(route, 0, 48.802483, 8.215782), new RoutePoint(route, 0, 48.802948, 8.216757), new RoutePoint(route, 0, 48.80336, 8.217407),
			        new RoutePoint(route, 0, 48.803841, 8.218505), new RoutePoint(route, 0, 48.804417, 8.219528),
			        new RoutePoint(route, 0, 48.805187, 8.220131), new RoutePoint(route, 0, 48.805756, 8.221105),
			        new RoutePoint(route, 0, 48.806305, 8.222058), new RoutePoint(route, 0, 48.806885, 8.22281), new RoutePoint(route, 0, 48.807163, 8.223117),
			        new RoutePoint(route, 0, 48.807693, 8.2238), new RoutePoint(route, 0, 48.808365, 8.224709), new RoutePoint(route, 0, 48.809086, 8.225434),
			        new RoutePoint(route, 0, 48.809875, 8.225866)

			};

			return rpoints;
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
