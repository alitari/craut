package de.craut.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.data.geo.Point;

import de.craut.domain.Activity;
import de.craut.domain.ActivityPoint;
import de.craut.domain.Route;
import de.craut.domain.RoutePoint;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;
import de.craut.util.geocalc.GpxUtils;

public class RouteMatchingTest extends AbstractServiceIntegrationTest {

	private static final String TEST_ROUTE = "Test-Route";

	@Test
	public void basic() throws Exception {

		assertThat(routeService.fetchAllRoutes().size(), is(0));
		List<? extends Point> points = Arrays.asList(new Point[] { new Point(65.34, 23.4644), new Point(65.35, 23.5644), new Point(65.36, 23.6644) });
		routeService.saveRoute(TEST_ROUTE, points);
		Route route = routeService.fetchAllRoutes().get(0);
		assertThat(route.getName(), is(TEST_ROUTE));
		List<RoutePoint> routePoints = routeService.fetchRoutePoints(route);
		assertThat(routePoints.get(0).getLatitude(), is(points.get(0).getX()));
		assertThat(routePoints.get(0).getLongitude(), is(points.get(0).getY()));

		List<GpxTrackPoint> trkPoints = Arrays.asList(new GpxTrackPoint[] { new GpxTrackPoint(65.34, 23.4644, new Date(), 0),
		        new GpxTrackPoint(65.35, 23.5644, new Date(), 0), new GpxTrackPoint(65.36, 23.6644, new Date(), 0) });
		Map<Activity, List<ActivityPoint>> activities = activityService.createActivities(trkPoints);
		Activity activity = activities.keySet().iterator().next();
		assertThat(activity.getRoute().getId(), is(route.getId()));

	}

	@Ignore
	public void files() throws Exception {
		Route freiolsheim = saveRoute("Malsch-Freiolsheim");
		Route roteLache = saveRoute("RoteLache2");

		assertMatch("22-08-2014", freiolsheim);
		assertMatch("24-04-2014", freiolsheim);
		assertMatch("30-04-2014", roteLache);
		assertMatch("08-05-2014", freiolsheim);
		assertMatch("14-05-2014", roteLache);
		assertMatch("16-05-2014", roteLache);
		assertMatch("25-05-2014", freiolsheim);
		assertMatch("28-05-2014", roteLache);
		assertMatch("05-06-2014", freiolsheim);
		assertMatch("07-06-2014", freiolsheim);

	}

	private void assertMatch(String activity, Route route) {
		String activityPath = "/gpx/activities/" + activity + ".gpx";
		List<GpxTrackPoint> gpxPointsActivity = GpxUtils.gpxFromFile(activityPath);
		Map<Activity, List<ActivityPoint>> activities = activityService.createActivities(gpxPointsActivity);
		Iterator<Activity> activityIter = activities.keySet().iterator();
		assertThat("No match for activity " + activity, activityIter.hasNext(), is(true));
		assertThat(activityIter.next().getRoute().getId(), is(route.getId()));
	}

	private Route saveRoute(String route) {
		String routePath = "/gpx/routes/" + route + ".gpx";
		List<GpxTrackPoint> gpxPointsRoute = GpxUtils.gpxFromFile(routePath);
		Route savedRoute = routeService.saveRoute(routePath, gpxPointsRoute);
		return savedRoute;
	}
}
