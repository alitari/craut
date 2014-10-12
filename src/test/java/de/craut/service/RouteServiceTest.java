package de.craut.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import de.craut.domain.ActivityPointRepository;
import de.craut.domain.ActivityRepository;
import de.craut.domain.FileUpload;
import de.craut.domain.FileUpload.Format;
import de.craut.domain.FileUpload.Type;
import de.craut.domain.FileUploadRepository;
import de.craut.domain.Route;
import de.craut.domain.RoutePoint;
import de.craut.domain.RoutePointRepository;
import de.craut.domain.RouteRepository;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;

public class RouteServiceTest extends ServiceTestWithRepositoryMocks<RouteService> {

	private static final long ID1 = 353;
	private static final String NAME1 = "Route1";

	@Test
	public void delete() {
		setupRoute(ID1, NAME1);
		Route deletedRoute = underTest.deleteRoute(ID1);
		assertThat(deletedRoute, is(allRoutes.get(0)));
		verify(routeRepository, times(1)).delete(ID1);

		List<RoutePoint> allRoutePoints = routePointRepository.findByRouteIdOrderBySequenceAsc(ID1);
		verify(routePointRepository, times(1)).delete(eq(allRoutePoints));
	}

	@Test
	public void getRoute() {
		setupRoute(ID1, NAME1);
		Route route = underTest.fetchRoute(ID1);
		assertThat(route, is(allRoutes.get(0)));
		verify(routeRepository, times(1)).findOne(ID1);
	}

	@Test
	public void update() {
		setupRoute(ID1, NAME1);
		Route event = new Route("zewgez");
		underTest.updateRoute(event);
		verify(routeRepository, times(1)).save(event);
	}

	@Test
	public void getAllRoutes() {
		setupRoute(ID1, NAME1);
		underTest.fetchAllRoutes();
		verify(routeRepository, times(1)).findAll();
	}

	@Test
	public void getRoutePoints() {
		setupRoute(ID1, NAME1);
		underTest.fetchRoutePoints(allRoutes.get(0));
		verify(routePointRepository, times(1)).findByRouteIdOrderBySequenceAsc(allRoutes.get(0).getId());
	}

	@Test
	public void saveRouteTakeAll() {
		List<GpxTrackPoint> points = Arrays.asList(new GpxTrackPoint[] { new GpxTrackPoint(3, 4), new GpxTrackPoint(3.4844, 4.9545) });
		underTest.saveRoute("testRoute", points);
		Route routeArgument = getRouteArgument();
		assertRoute(routeArgument, "testRoute", points.get(0), points.get(1));
		List<RoutePoint> routePoints = getRoutePointListArgument();
		assertTakeOver(points, routePoints);
	}

	@Test
	public void saveRouteSkipMiddle() {
		List<GpxTrackPoint> points = Arrays.asList(new GpxTrackPoint[] { //
		        new GpxTrackPoint(3, 4), new GpxTrackPoint(3 + latitudeMeter, 4 + longitudeMeter), new GpxTrackPoint(3.4844, 4.9545) //
		        });

		underTest.saveRoute("testRoute", points);
		Route routeArgument = getRouteArgument();
		assertRoute(routeArgument, "testRoute", points.get(0), points.get(2));
		List<RoutePoint> routePoints = getRoutePointListArgument();
		assertTakeOver(points, routePoints, 0, 2); // 1 is skipped
	}

	@Test
	public void saveRouteSkipMoreMiddle() {
		List<GpxTrackPoint> points = Arrays.asList(new GpxTrackPoint[] { //
		        new GpxTrackPoint(3, 4), // 0
		                new GpxTrackPoint(3 + latitudeMeter, 4 + longitudeMeter),// 1
		                new GpxTrackPoint(3 + 2 * latitudeMeter, 4 + 3 * longitudeMeter),// 2
		                new GpxTrackPoint(3 + 10 * latitudeMeter, 4 + 10 * longitudeMeter),// 3
		                new GpxTrackPoint(3 + 21 * latitudeMeter, 4 + 21 * longitudeMeter),// 4
		                new GpxTrackPoint(3 + 70 * latitudeMeter, 4 + 70 * longitudeMeter) // 5
		        });

		underTest.saveRoute("testRoute", points);
		Route routeArgument = getRouteArgument();
		List<RoutePoint> routePoints = getRoutePointListArgument();

		assertRoute(routeArgument, "testRoute", points.get(0), points.get(5));
		assertTakeOver(points, routePoints, 0, 4, 5); // 1,2,3 is
		                                              // skipped
	}

	@Test
	public void saveRouteSkipSomeMiddleAndEnd() {
		List<GpxTrackPoint> points = Arrays.asList(new GpxTrackPoint[] { //
		        new GpxTrackPoint(3, 4), // 0
		                new GpxTrackPoint(3 + latitudeMeter, 4 + longitudeMeter),// 1
		                new GpxTrackPoint(3 + 40 * latitudeMeter, 4 + 3 * longitudeMeter),// 2
		                new GpxTrackPoint(3 + 41 * latitudeMeter, 4 + 3 * longitudeMeter),// 3
		                new GpxTrackPoint(3 + 60 * latitudeMeter, 4 + 20 * longitudeMeter),// 4
		                new GpxTrackPoint(3 + 70 * latitudeMeter, 4 + 30 * longitudeMeter) // 5
		        });

		underTest.saveRoute("testRoute", points);
		Route routeArgument = getRouteArgument();
		assertRoute(routeArgument, "testRoute", points.get(0), points.get(4));
		List<RoutePoint> routePoints = getRoutePointListArgument();
		assertTakeOver(points, routePoints, 0, 2, 4); // 1,3,5 is skipped
	}

	private void assertRoute(Route routeArgument, String name, GpxTrackPoint startPoint, GpxTrackPoint endPoint) {
		assertThat(routeArgument.getName(), is(name));
		assertThat(routeArgument.getStartLatitude(), is(startPoint.getLatitude()));
		assertThat(routeArgument.getStartLongitude(), is(startPoint.getLongitude()));
		assertThat(routeArgument.getEndLatitude(), is(endPoint.getLatitude()));
		assertThat(routeArgument.getEndLongitude(), is(endPoint.getLongitude()));
	}

	private void assertTakeOver(List<GpxTrackPoint> points, List<RoutePoint> routePoints, int... indices) {
		for (int i = 0; i < routePoints.size(); i++) {
			int gpxPointIndex = ArrayUtils.isEmpty(indices) ? i : indices[i];
			RoutePoint routePoint = routePoints.get(i);
			GpxTrackPoint gpxTrackPoint = points.get(gpxPointIndex);
			assertThat(routePoint.getX(), is(gpxTrackPoint.getX()));
			assertThat(routePoint.getY(), is(gpxTrackPoint.getY()));
		}
	}

	private List<RoutePoint> getRoutePointListArgument() {
		ArgumentCaptor<Iterable> routePointsCapture = ArgumentCaptor.forClass(Iterable.class);
		verify(routePointRepository, times(1)).save(routePointsCapture.capture());
		List<RoutePoint> routePoints = (List<RoutePoint>) routePointsCapture.getValue();
		return routePoints;
	}

	private Route getRouteArgument() {
		ArgumentCaptor<Route> routeCapture = ArgumentCaptor.forClass(Route.class);
		verify(routeRepository, times(1)).save(routeCapture.capture());
		Route routeArgument = routeCapture.getValue();
		return routeArgument;
	}

	@Test
	public void fileUpload() {
		byte[] content = new byte[] { 1, 2, 3, 4, Byte.MAX_VALUE, Byte.MIN_VALUE };
		underTest.fileUpload(content, Type.Activity, Format.GPX);
		String thread = String.valueOf(Thread.currentThread().getId()) + " " + Thread.currentThread().getName();

		ArgumentCaptor<FileUpload> fileUploadCaptor = ArgumentCaptor.forClass(FileUpload.class);
		verify(fileUploadRepository, times(1)).save(fileUploadCaptor.capture());
		assertThat(fileUploadCaptor.getValue().getContent(), is(content));
		assertThat(fileUploadCaptor.getValue().getThread(), is(thread));
		assertThat(fileUploadCaptor.getValue().getFormat(), is(Format.GPX));
		assertThat(fileUploadCaptor.getValue().getType(), is(Type.Activity));
	}

	@Test
	public void fetchFileUploads() throws Exception {
		List<FileUpload> fileUploads = new ArrayList<FileUpload>();
		FileUpload fileUpload = new FileUpload("thread", 2, new byte[] { 1, 2, 34 }, new Date(), Type.Activity, Format.GPX);
		fileUploads.add(fileUpload);
		when(fileUploadRepository.findByInsertDateGreaterThan(any(Date.class))).thenReturn(fileUploads);

		underTest.fetchFileUploadsFromToday();
		ArgumentCaptor<Date> dateCaptor = ArgumentCaptor.forClass(Date.class);
		verify(fileUploadRepository, times(1)).findByInsertDateGreaterThan(dateCaptor.capture());
		assertThat(DateUtils.isSameDay(dateCaptor.getValue(), new Date()), is(true));
		assertThat(DateUtils.getFragmentInSeconds(dateCaptor.getValue(), Calendar.DAY_OF_MONTH), is((long) 0));
	}

	@Override
	protected RouteService createService(ActivityRepository activityRepository, ActivityPointRepository activityPointRepository,
	        RouteRepository routeRepository, RoutePointRepository routePointRepository, FileUploadRepository fileUploadRepository) {
		return new RouteService(routeRepository, routePointRepository, fileUploadRepository);
	}

}
