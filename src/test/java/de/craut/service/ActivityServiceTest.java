package de.craut.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import de.craut.domain.Activity;
import de.craut.domain.ActivityPoint;
import de.craut.domain.ActivityPointRepository;
import de.craut.domain.ActivityRepository;
import de.craut.domain.FileUploadRepository;
import de.craut.domain.Route;
import de.craut.domain.RoutePoint;
import de.craut.domain.RoutePointRepository;
import de.craut.domain.RouteRepository;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;

public class ActivityServiceTest extends ServiceTestWithRepositoryMocks<ActivityService> {

	private double startLatitude;
	private double startLongitude;
	private double endLatitude;
	private double endLongitude;

	@Override
	@Before
	public void setup() {
		super.setup();

		startLatitude = 48.796108;
		startLongitude = 8.202952;
		endLatitude = 48.800747;
		endLongitude = 8.212094;

	}

	@Override
	protected ActivityService createService(ActivityRepository activityRepository, ActivityPointRepository activityPointRepository,
	        RouteRepository routeRepository, RoutePointRepository routePointRepository, FileUploadRepository fileUploadRepository) {
		return new ActivityService(activityRepository, activityPointRepository, routeRepository, routePointRepository);
	}

	private List<GpxTrackPoint> createTrackPoints() {
		List<GpxTrackPoint> trackPoints = Arrays.asList(new GpxTrackPoint[] {
		        new GpxTrackPoint(startLatitude + latitudeMeter, startLongitude + longitudeMeter, createToday(1), 0),
		        new GpxTrackPoint(startLatitude + 30 * latitudeMeter, startLongitude + 20 * longitudeMeter, createToday(8), 0),
		        new GpxTrackPoint(endLatitude - 2 * latitudeMeter, endLongitude - 2 * longitudeMeter, createToday(30 * 60), 0) });
		return trackPoints;
	}

	@Test
	public void routeMatch1() throws Exception {

		Route route = setupRoute(1, "route1", startLatitude, startLongitude, endLatitude, endLongitude);

		List<GpxTrackPoint> trackPoints = createTrackPoints();

		Map<Activity, List<ActivityPoint>> activities = underTest.createActivities(trackPoints);
		List<RoutePoint> routePoints = routePointRepository.findByRoute(route);

		assertThat(activities.size(), is(1));
		Entry<Activity, List<ActivityPoint>> activityEntry = activities.entrySet().iterator().next();
		Activity activity = activityEntry.getKey();
		assertThat(activity.getRoute(), is(route));
		List<ActivityPoint> pointList = activityEntry.getValue();
		assertThat(pointList.size(), is(routePoints.size()));

		checkActivityPoint(pointList.get(0), routePoints.get(0), 0, trackPoints.get(0));
		checkActivityPoint(pointList.get(1), routePoints.get(1), 1, trackPoints.get(2));

	}

	private void checkActivityPoint(ActivityPoint activityPoint, RoutePoint routePoint, int seq, GpxTrackPoint trkPoint) {
		assertThat(activityPoint.getActivity().getRoute(), is(routePoint.getRoute()));
		assertThat(activityPoint.getRoutePoint(), is(routePoint));
		assertThat(activityPoint.getTime(), is(trkPoint.time));
	}

	private Date createToday(int s) {
		Date dayStart = DateUtils.truncate(new Date(), Calendar.DATE);
		return DateUtils.addSeconds(dayStart, s);
	}

}
