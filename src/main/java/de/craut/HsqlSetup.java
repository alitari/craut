package de.craut;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
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
import de.craut.service.ChallengeService;
import de.craut.service.RouteService;
import de.craut.util.geocalc.GPXParser;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;

@Configuration
@Profile("hsql")
public class HsqlSetup {

	private static class InitHsqlDb implements InitDB {

		@Override
		public void init(ApplicationContext ctx) {

			List<User> users = createUsers(ctx, "Tick", "Trick", "Track");

			List<Route> routes = createRoutes(ctx, "/gpx/routes");

			createActivityFromRoute(ctx, routes.get(0), users.get(0));
			createActivityFromRoute(ctx, routes.get(1), users.get(0));
			createActivityFromRoute(ctx, routes.get(2), users.get(0));
			createActivityFromRoute(ctx, routes.get(3), users.get(0));

			createActivityFromRoute(ctx, routes.get(2), users.get(1));
			createActivityFromRoute(ctx, routes.get(3), users.get(1));
			createActivityFromRoute(ctx, routes.get(4), users.get(1));
			createActivityFromRoute(ctx, routes.get(5), users.get(1));
			createActivityFromRoute(ctx, routes.get(6), users.get(1));
			createActivityFromRoute(ctx, routes.get(7), users.get(1));

			createActivityFromRoute(ctx, routes.get(2), users.get(2));
			createActivityFromRoute(ctx, routes.get(3), users.get(2));
			createActivityFromRoute(ctx, routes.get(4), users.get(2));
			createActivityFromRoute(ctx, routes.get(9), users.get(2));
			createActivityFromRoute(ctx, routes.get(10), users.get(2));
			createActivityFromRoute(ctx, routes.get(11), users.get(2));

			ChallengeService challengeService = ctx.getBean(ChallengeService.class);
			challengeService.saveChallenge("Challenge " + routes.get(2).getName(), routes.get(2));

		}

		private List<Route> createRoutes(ApplicationContext ctx, String path) {
			ArrayList<Route> savedRoutes = new ArrayList<Route>();
			URL dirURL = getClass().getResource(path);
			String[] routeFiles = null;
			try {
				routeFiles = new File(dirURL.toURI()).list();
				for (int i = 0; i < routeFiles.length; i++) {
					Route saveRoute = saveRoute(ctx, path, routeFiles[i]);
					savedRoutes.add(saveRoute);
				}

			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}

			return savedRoutes;
		}

		private List<User> createUsers(ApplicationContext ctx, String... users) {
			ArrayList<User> list = new ArrayList<User>();
			UserRepository userRepo = ctx.getBean(UserRepository.class);
			for (int i = 0; i < users.length; i++) {
				User user = new User(users[i], users[i]);
				userRepo.save(user);
				list.add(user);
			}
			return list;
		}

		private Activity createActivityFromRoute(ApplicationContext ctx, Route route, User user) {
			Date start = DateUtils.addMinutes(new Date(), -20);
			Date end = new Date();

			ActivityRepository activityRepository = ctx.getBean(ActivityRepository.class);
			Activity activity = new Activity(DateFormatUtils.format(start, "dd.MM.yy"), user, route, start.getTime(), end.getTime());
			activity = activityRepository.save(activity);

			List<RoutePoint> rpList = ctx.getBean(RouteService.class).fetchRoutePoints(route);
			List<ActivityPoint> apList = new ArrayList<ActivityPoint>();

			Random random = new Random();

			for (int i = 0; i < rpList.size(); i++) {
				RoutePoint routePoint = rpList.get(i);
				routePoint.setSequence(i);
				ActivityPoint activityPoint = new ActivityPoint(routePoint, start, 10 + 10 * random.nextDouble(), 0, 0, 0);
				activityPoint.setActivityId(activity.getId());
				start = DateUtils.addSeconds(start, 1 + random.nextInt(4));
				apList.add(activityPoint);
			}

			ActivityPointRepository activityPointRepo = ctx.getBean(ActivityPointRepository.class);
			activityPointRepo.save(apList);
			return activity;
		}

		private Route saveRoute(ApplicationContext ctx, String path, String gpxFileName) {
			InputStream gpxIs = getClass().getResourceAsStream(path + "/" + gpxFileName);
			GPXParser gpxParser = new GPXParser();
			List<GpxTrackPoint> points = gpxParser.parse(gpxIs);
			RouteService routeService = ctx.getBean(RouteService.class);
			Route saveRoute = routeService.saveRoute(gpxFileName, points);
			return saveRoute;
		}

	}

	@Bean
	public InitDB initDB() {
		return new InitHsqlDb();
	}

}
