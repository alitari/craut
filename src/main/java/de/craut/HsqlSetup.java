package de.craut;

import java.io.InputStream;
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
import org.springframework.data.geo.Point;

import de.craut.domain.Activity;
import de.craut.domain.ActivityPoint;
import de.craut.domain.ActivityPointRepository;
import de.craut.domain.ActivityRepository;
import de.craut.domain.Route;
import de.craut.domain.RoutePoint;
import de.craut.service.RouteService;
import de.craut.util.geocalc.GPXParser;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;

@Configuration
@Profile("hsql")
public class HsqlSetup {

	private static class InitHsqlDb implements InitDB {

		@Override
		public void init(ApplicationContext ctx) {
			RouteService routeService = ctx.getBean(RouteService.class);

			Route savedRoute = routeService.saveRoute("Baden-Baden", Arrays.asList(createPoints()));

			Date start = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
			ActivityRepository activityRepository = ctx.getBean(ActivityRepository.class);
			Activity activity = new Activity("Baden Baden Act", savedRoute, start);
			activityRepository.save(activity);

			List<RoutePoint> rpList = routeService.fetchRoutePoints(savedRoute);
			List<ActivityPoint> apList = new ArrayList<ActivityPoint>();

			for (int i = 0; i < rpList.size(); i++) {
				RoutePoint routePoint = rpList.get(i);
				routePoint.setSequence(i);
				ActivityPoint activityPoint = new ActivityPoint(activity, routePoint, start);
				start = DateUtils.addSeconds(start, 1);
			}

			ActivityPointRepository activityPointRepo = ctx.getBean(ActivityPointRepository.class);
			activityPointRepo.save(apList);

			saveRoute(ctx, "Malsch-Freiolsheim.gpx");
			saveRoute(ctx, "RoteLache.gpx");

		}

		private void saveRoute(ApplicationContext ctx, String gpxFileName) {
			InputStream gpxIs = getClass().getResourceAsStream("/gpx/routes/" + gpxFileName);
			GPXParser gpxParser = new GPXParser();
			List<GpxTrackPoint> points = gpxParser.parse(gpxIs);
			RouteService routeService = ctx.getBean(RouteService.class);
			routeService.saveRoute(gpxFileName, points);
		}

		private Point[] createPoints() {
			Point[] rpoints = new Point[] { new Point(48.783405, 8.20354), new Point(48.783775, 8.202754), new Point(48.784084, 8.202045),
			        new Point(48.784477, 8.201142), new Point(48.784767, 8.200213), new Point(48.785069, 8.1992), new Point(48.785572, 8.198301),
			        new Point(48.786045, 8.197291), new Point(48.786533, 8.196348), new Point(48.787243, 8.195672), new Point(48.788071, 8.195345),
			        new Point(48.788898, 8.195307), new Point(48.78957, 8.194707), new Point(48.790154, 8.194908), new Point(48.79073, 8.195398),
			        new Point(48.79126, 8.196105), new Point(48.791836, 8.196837), new Point(48.792477, 8.197448), new Point(48.793125, 8.198131),
			        new Point(48.793804, 8.19834), new Point(48.794422, 8.199176), new Point(48.795067, 8.199977), new Point(48.795124, 8.201135),
			        new Point(48.795509, 8.202103), new Point(48.796108, 8.202952), new Point(48.796749, 8.203724), new Point(48.797241, 8.204524),
			        new Point(48.797958, 8.20512), new Point(48.798645, 8.205972), new Point(48.799217, 8.206955), new Point(48.799641, 8.208044),
			        new Point(48.799957, 8.209209), new Point(48.800255, 8.210369), new Point(48.800564, 8.211535), new Point(48.800938, 8.212642),
			        new Point(48.801388, 8.213676), new Point(48.801929, 8.214728), new Point(48.802483, 8.215782), new Point(48.802948, 8.216757),
			        new Point(48.80336, 8.217407), new Point(48.803841, 8.218505), new Point(48.804417, 8.219528), new Point(48.805187, 8.220131),
			        new Point(48.805756, 8.221105), new Point(48.806305, 8.222058), new Point(48.806885, 8.22281), new Point(48.807163, 8.223117),
			        new Point(48.807693, 8.2238), new Point(48.808365, 8.224709), new Point(48.809086, 8.225434), new Point(48.809875, 8.225866)

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
