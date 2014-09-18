package de.craut.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.craut.domain.Route;
import de.craut.domain.RouteActivity;
import de.craut.domain.RoutePoint;
import de.craut.domain.RoutePointRepository;
import de.craut.domain.RouteRepository;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;

public class RouteServiceTest {

	private static final long ID1 = 353;
	private static final String NAME1 = "Route1";
	RouteService underTest;
	private List<Route> allRoutes;
	private RouteRepository routeRepository;
	private RoutePointRepository routePointRepository;
	private RoutePoint[] routePoints;

	@Before
	public void setup() {
		allRoutes = new ArrayList<Route>();
		routeRepository = mock(RouteRepository.class);
		routePointRepository = mock(RoutePointRepository.class);

		setupRoute(ID1, NAME1, "48.796108", "8.202952", "48.796108", "8.202952", "48.796108", "8.202952");

		when(routeRepository.findAll()).thenReturn(allRoutes);

		underTest = new RouteService(routeRepository, routePointRepository);
	}

	private void setupRoute(long id, String name, String... points) {
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

	}

	@Test
	public void create() {
		String name = "Testname";
		underTest.createRoute(name);
		verify(routeRepository, times(1)).save(any(Route.class));
	}

	@Test
	public void delete() {
		Route deletedRoute = underTest.deleteRoute(ID1);
		assertThat(deletedRoute, is(allRoutes.get(0)));
		verify(routeRepository, times(1)).delete(ID1);
	}

	@Test
	public void getRoute() {
		Route route = underTest.getRoute(ID1);
		assertThat(route, is(allRoutes.get(0)));
		verify(routeRepository, times(1)).findOne(ID1);
	}

	@Test
	public void update() {
		Route event = new Route("zewgez", new Date());
		underTest.updateRoute(event);
		verify(routeRepository, times(1)).save(event);
	}

	@Test
	public void getAllRoutes() {
		underTest.getAllRoutes();
		verify(routeRepository, times(1)).findAll();
	}

	@Test
	public void getRoutePoints() {
		underTest.getRoutePoints(allRoutes.get(0));
		verify(routePointRepository, times(1)).findByRoute(allRoutes.get(0));
	}

	@Test
	public void routeMatch() throws Exception {
		Iterator<GpxTrackPoint> gpxTrcks = null;
		List<RouteActivity> activities = underTest.createActivities(gpxTrcks);

	}

}
