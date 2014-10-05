package de.craut.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.geo.Point;

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

public class RouteServiceTest extends ServiceTestWithRepositoryMocks<RouteService> {

	private static final long ID1 = 353;
	private static final String NAME1 = "Route1";

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
		Route route = underTest.fetchRoute(ID1);
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
		underTest.fetchAllRoutes();
		verify(routeRepository, times(1)).findAll();
	}

	@Test
	public void getRoutePoints() {
		setupRoute(ID1, NAME1);
		underTest.fetchRoutePoints(allRoutes.get(0));
		verify(routePointRepository, times(1)).findByRoute(allRoutes.get(0));
	}

	@Test
	public void saveRoute() {

		List<Point> points = Arrays.asList(new Point[] { new Point(3, 4), new Point(3.4844, 4.9545) });
		underTest.saveRoute("testRoute", points);
		ArgumentCaptor<Route> routeCapture = ArgumentCaptor.forClass(Route.class);

		verify(routeRepository, times(1)).save(routeCapture.capture());
		Route routeArgument = routeCapture.getValue();
		assertThat(routeArgument.getName(), is("testRoute"));
		assertThat(routeArgument.getStartLatitude(), is(points.get(0).getX()));
		assertThat(routeArgument.getStartLongitude(), is(points.get(0).getY()));
		assertThat(routeArgument.getEndLatitude(), is(points.get(points.size() - 1).getX()));
		assertThat(routeArgument.getEndLongitude(), is(points.get(points.size() - 1).getY()));

		ArgumentCaptor<Iterable> routePointsCapture = ArgumentCaptor.forClass(Iterable.class);
		verify(routePointRepository, times(1)).save(routePointsCapture.capture());
		Iterator iterator = routePointsCapture.getValue().iterator();
		RoutePoint rp = ((RoutePoint) iterator.next());
		assertThat(rp.getX(), is(points.get(0).getX()));
		assertThat(rp.getY(), is(points.get(0).getY()));

		rp = ((RoutePoint) iterator.next());
		assertThat(rp.getX(), is(points.get(points.size() - 1).getX()));
		assertThat(rp.getY(), is(points.get(points.size() - 1).getY()));

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
