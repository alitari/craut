package de.craut.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import de.craut.domain.Activity;
import de.craut.domain.ActivityPoint;
import de.craut.domain.FileUpload;
import de.craut.domain.FileUploadRepository;
import de.craut.domain.Route;
import de.craut.domain.RoutePoint;
import de.craut.domain.RoutePointRepository;
import de.craut.domain.RouteRepository;
import de.craut.service.RouteService.GpxStatistics;
import de.craut.util.geocalc.EarthCalc;
import de.craut.util.geocalc.GPXParser;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;

public class RouteServiceTest {

	private static final long ID1 = 353;
	private static final String NAME1 = "Route1";
	RouteService underTest;
	private List<Route> allRoutes;
	private RouteRepository routeRepository;
	private RoutePointRepository routePointRepository;
	private FileUploadRepository fileUploadRepository;

	private final static double latitudeMeter = 0.000009;
	private final static double longitudeMeter = 0.000014;
	private double startLatitude;
	private double startLongitude;
	private double endLatitude;
	private double endLongitude;

	@Before
	public void setup() {

		startLatitude = 48.796108;
		startLongitude = 8.202952;
		endLatitude = 48.800747;
		endLongitude = 8.212094;

		allRoutes = new ArrayList<Route>();
		routeRepository = mock(RouteRepository.class);
		routePointRepository = mock(RoutePointRepository.class);
		fileUploadRepository = mock(FileUploadRepository.class);

		when(routeRepository.findAll()).thenReturn(allRoutes);

		underTest = new RouteService(routeRepository, routePointRepository, fileUploadRepository);
	}

	private List<GpxTrackPoint> gpxFromFile(String resourcePath) {
		InputStream gpxIs = getClass().getResourceAsStream(resourcePath); //

		GPXParser gpxParser = new GPXParser();
		List<GpxTrackPoint> gpxPoints = gpxParser.parse(gpxIs);
		return gpxPoints;
	}

	private Route setupRoute(long id, String name, double... points) {
		Route route = new Route(name, new Date());
		allRoutes.add(route);
		when(routeRepository.findOne(id)).thenReturn(route);
		List<RoutePoint> routePoints = new ArrayList<RoutePoint>();
		int i = 0;
		while (i < points.length) {
			routePoints.add(new RoutePoint(route, i / 2, points[i], points[i + 1]));
			i += 2;
		}
		when(routePointRepository.findByRoute(route)).thenReturn(routePoints);
		return route;

	}

	@Test
	public void create() {
		setupRoute(ID1, NAME1);
		String name = "Testname";
		underTest.createRoute(name);
		verify(routeRepository, times(1)).save(any(Route.class));
	}

	@Test
	public void delete() {
		setupRoute(ID1, NAME1);
		Route deletedRoute = underTest.deleteRoute(ID1);
		assertThat(deletedRoute, is(allRoutes.get(0)));
		verify(routeRepository, times(1)).delete(ID1);
	}

	@Test
	public void getRoute() {
		setupRoute(ID1, NAME1);
		Route route = underTest.getRoute(ID1);
		assertThat(route, is(allRoutes.get(0)));
		verify(routeRepository, times(1)).findOne(ID1);
	}

	@Test
	public void update() {
		setupRoute(ID1, NAME1);
		Route event = new Route("zewgez", new Date());
		underTest.updateRoute(event);
		verify(routeRepository, times(1)).save(event);
	}

	@Test
	public void getAllRoutes() {
		setupRoute(ID1, NAME1);
		underTest.getAllRoutes();
		verify(routeRepository, times(1)).findAll();
	}

	@Test
	public void getRoutePoints() {
		setupRoute(ID1, NAME1);
		underTest.getRoutePoints(allRoutes.get(0));
		verify(routePointRepository, times(1)).findByRoute(allRoutes.get(0));
	}

	@Test
	public void fileUpload() {
		byte[] content = new byte[] { 1, 2, 3, 4, Byte.MAX_VALUE, Byte.MIN_VALUE };
		underTest.fileUpload(content);
		String thread = String.valueOf(Thread.currentThread().getId()) + " " + Thread.currentThread().getName();

		ArgumentCaptor<FileUpload> fileUploadCaptor = ArgumentCaptor.forClass(FileUpload.class);
		verify(fileUploadRepository, times(1)).save(fileUploadCaptor.capture());
		assertThat(fileUploadCaptor.getValue().getContent(), is(content));
		assertThat(fileUploadCaptor.getValue().getThread(), is(thread));
	}

	@Test
	public void fetchFileUploads() throws Exception {
		List<FileUpload> fileUploads = new ArrayList<FileUpload>();
		FileUpload fileUpload = new FileUpload("thread", 2, new byte[] { 1, 2, 34 }, new Date());
		fileUploads.add(fileUpload);
		when(fileUploadRepository.findByInsertDateGreaterThan(any(Date.class))).thenReturn(fileUploads);

		underTest.fetchFileUploadsFromToday();
		ArgumentCaptor<Date> dateCaptor = ArgumentCaptor.forClass(Date.class);
		verify(fileUploadRepository, times(1)).findByInsertDateGreaterThan(dateCaptor.capture());
		assertThat(DateUtils.isSameDay(dateCaptor.getValue(), new Date()), is(true));
		assertThat(DateUtils.getFragmentInSeconds(dateCaptor.getValue(), Calendar.DAY_OF_MONTH), is((long) 0));
	}

	@Test
	public void activityPointsMatch1() throws Exception {

		Route route = setupRoute(1, "route1", startLatitude, startLongitude, endLatitude, endLongitude);

		List<GpxTrackPoint> trackPoints = Arrays.asList(new GpxTrackPoint[] {
		        new GpxTrackPoint(startLatitude + latitudeMeter, startLongitude + longitudeMeter, createToday(1), 0),
		        new GpxTrackPoint(startLatitude + 15 * latitudeMeter, startLongitude + 11 * longitudeMeter, createToday(8), 0),
		        new GpxTrackPoint(endLatitude - 2 * latitudeMeter, endLongitude - 2 * longitudeMeter, createToday(30 * 60), 0) });

		List<ActivityPoint> pointList = underTest.createActivityPoints(trackPoints, route, 5);

		List<RoutePoint> routePoints = underTest.getRoutePoints(route);
		assertThat(pointList.size(), is(routePoints.size()));

		checkActivityPoint(pointList.get(0), routePoints.get(0), 0, trackPoints.get(0));
		checkActivityPoint(pointList.get(1), routePoints.get(1), 1, trackPoints.get(2));
	}

	@Test
	public void routeMatch1() throws Exception {

		Route route = setupRoute(1, "route1", startLatitude, startLongitude, endLatitude, endLongitude);

		List<GpxTrackPoint> trackPoints = Arrays.asList(new GpxTrackPoint[] {
		        new GpxTrackPoint(startLatitude + latitudeMeter, startLongitude + longitudeMeter, createToday(1), 0),
		        new GpxTrackPoint(startLatitude + 15 * latitudeMeter, startLongitude + 11 * longitudeMeter, createToday(8), 0),
		        new GpxTrackPoint(endLatitude - 2 * latitudeMeter, endLongitude - 2 * longitudeMeter, createToday(30 * 60), 0) });

		Map<Activity, List<ActivityPoint>> activities = underTest.createActivities(trackPoints);
		List<RoutePoint> routePoints = underTest.getRoutePoints(route);

		assertThat(activities.size(), is(1));
		Entry<Activity, List<ActivityPoint>> activityEntry = activities.entrySet().iterator().next();
		Activity activity = activityEntry.getKey();
		assertThat(activity.getRoute(), is(route));
		List<ActivityPoint> pointList = activityEntry.getValue();
		assertThat(pointList.size(), is(routePoints.size()));

		checkActivityPoint(pointList.get(0), routePoints.get(0), 0, trackPoints.get(0));
		checkActivityPoint(pointList.get(1), routePoints.get(1), 1, trackPoints.get(2));

	}

	@Test
	public void activityPointsMatchFromFile1() throws Exception {
		List<GpxTrackPoint> trackPoints = gpxFromFile("/gpx/test2.gpx");
		Route route = setupRoute(1, "route1", startLatitude, startLongitude, endLatitude, endLongitude);

		List<ActivityPoint> pointList = underTest.createActivityPoints(trackPoints, route, 5);

		List<RoutePoint> routePoints = underTest.getRoutePoints(route);
		assertThat(pointList.size(), is(routePoints.size()));

		checkActivityPoint(pointList.get(0), routePoints.get(0), 0, trackPoints.get(289));
		checkActivityPoint(pointList.get(1), routePoints.get(1), 1, trackPoints.get(384));

	}

	@Test
	public void getStatistics() throws Exception {
		GpxStatistics statistics = underTest.getStatistics(gpxFromFile("/gpx/test2.gpx"));
		assertThat(statistics.minDistance, is(4.828659858923302));
		assertThat(statistics.maxDistance, is(11.125689133761142));

	}

	private void checkActivityPoint(ActivityPoint activityPoint, RoutePoint routePoint, int seq, GpxTrackPoint trkPoint) {
		assertThat(activityPoint.getActivity().getRoute(), is(routePoint.getRoute()));
		assertThat(activityPoint.getRoutePoint(), is(routePoint));
		assertThat(activityPoint.getTime(), is(trkPoint.time.getTime()));
	}

	@Test
	public void activityPointsNoMatch1() throws Exception {

		setupRoute(1, "route1", startLatitude, startLongitude, endLatitude, endLongitude);

		List<GpxTrackPoint> trackPoints = Arrays.asList(new GpxTrackPoint[] {
		        new GpxTrackPoint(startLatitude + latitudeMeter, startLongitude + longitudeMeter, createToday(1), 0),
		        new GpxTrackPoint(startLatitude + 15 * latitudeMeter, startLongitude + 11 * longitudeMeter, createToday(8), 0),
		        new GpxTrackPoint(endLatitude - 6 * latitudeMeter, endLongitude - 5 * longitudeMeter, createToday(30 * 60), 0) });

		Route route = allRoutes.get(0);
		List<ActivityPoint> pointList = underTest.createActivityPoints(trackPoints, route, 5);

		assertThat(pointList.size(), is(0));

	}

	private Calendar createToday(long s) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR, (int) s / 3600);
		calendar.set(Calendar.MINUTE, (int) (s / 60) % 60);
		calendar.set(Calendar.SECOND, (int) s % 60);
		return calendar;
	}

	@Test
	public void distances() throws Exception {
		RoutePoint point1 = new RoutePoint(null, 0, "48", "8");
		RoutePoint point2 = new RoutePoint(null, 0, "49", "8");
		double distance = EarthCalc.getDistance(point1, point2);
		assertThat((int) (distance / 1000), is(111));

		point1 = new RoutePoint(null, 0, "48", "8");
		point2 = new RoutePoint(null, 0, "48", "9");
		distance = EarthCalc.getDistance(point1, point2);
		assertThat((int) (distance / 1000), is(74));

		// 1m latitude
		point1 = new RoutePoint(null, 0, "48", "8");
		point2 = new RoutePoint(null, 0, "48.000009", "8");
		distance = EarthCalc.getDistance(point1, point2);
		assertThat(distance, is((double) 1.0002069982284665));

		// 1m longitude
		point1 = new RoutePoint(null, 0, "48", "8");
		point2 = new RoutePoint(null, 0, "48", "8.000014");
		distance = EarthCalc.getDistance(point1, point2);
		assertThat(distance, is((double) 1.0399657163295126));

	}
}
