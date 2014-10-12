package de.craut;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import de.craut.domain.Activity;
import de.craut.domain.ActivityPoint;
import de.craut.domain.ActivityPointRepository;
import de.craut.domain.ActivityRepository;
import de.craut.domain.Route;
import de.craut.domain.RoutePoint;
import de.craut.domain.User;
import de.craut.domain.UserRepository;
import de.craut.service.RouteService;
import de.craut.util.geocalc.GPXParser;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;

@Configuration
@Profile("hsql")
public class HsqlSetup {

	private static class InitHsqlDb implements InitDB {

		@Override
		public void init(ApplicationContext ctx) {

			UserRepository userRepo = ctx.getBean(UserRepository.class);

			User user = new User("Alex", "mine");
			userRepo.save(user);

			Route savedRoute = saveRoute(ctx, "Baden-Baden-RoteLache.gpx");
			createActivityFromRoute(ctx, savedRoute);

			// savedRoute = saveRoute(ctx, "Malsch-Freiolsheim.gpx");
			// createActivityFromRoute(ctx, savedRoute);
			//
			// savedRoute = saveRoute(ctx, "RoteLache.gpx");
			// createActivityFromRoute(ctx, savedRoute);

		}

		private void createActivityFromRoute(ApplicationContext ctx, Route route) {
			Date start = DateUtils.addMinutes(new Date(), -20);
			Date end = new Date();

			ActivityRepository activityRepository = ctx.getBean(ActivityRepository.class);
			Activity activity = new Activity(DateFormatUtils.format(start, "dd.MM.yy"), route, start.getTime(), end.getTime());
			activity = activityRepository.save(activity);

			List<RoutePoint> rpList = ctx.getBean(RouteService.class).fetchRoutePoints(route);
			List<ActivityPoint> apList = new ArrayList<ActivityPoint>();

			Random random = new Random();

			for (int i = 0; i < rpList.size(); i++) {
				RoutePoint routePoint = rpList.get(i);
				routePoint.setSequence(i);
				ActivityPoint activityPoint = new ActivityPoint(routePoint, start, 10 + 10 * random.nextDouble(), 0, 0, 0);
				activityPoint.setActivityId(activity.getId());
				start = DateUtils.addSeconds(start, 1);
				apList.add(activityPoint);
			}

			ActivityPointRepository activityPointRepo = ctx.getBean(ActivityPointRepository.class);
			activityPointRepo.save(apList);
		}

		private Route saveRoute(ApplicationContext ctx, String gpxFileName) {
			InputStream gpxIs = getClass().getResourceAsStream("/gpx/routes/" + gpxFileName);
			GPXParser gpxParser = new GPXParser();
			List<GpxTrackPoint> points = gpxParser.parse(gpxIs);
			RouteService routeService = ctx.getBean(RouteService.class);
			Route saveRoute = routeService.saveRoute(gpxFileName, points);
			return saveRoute;
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
