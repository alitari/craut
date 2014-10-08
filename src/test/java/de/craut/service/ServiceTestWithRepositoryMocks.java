package de.craut.service;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

import de.craut.domain.ActivityPointRepository;
import de.craut.domain.ActivityRepository;
import de.craut.domain.FileUploadRepository;
import de.craut.domain.Route;
import de.craut.domain.RoutePoint;
import de.craut.domain.RoutePointRepository;
import de.craut.domain.RouteRepository;

public abstract class ServiceTestWithRepositoryMocks<T> {

	protected T underTest;

	protected List<Route> allRoutes;

	protected ActivityRepository activityRepository;
	protected RouteRepository routeRepository;
	protected RoutePointRepository routePointRepository;
	protected FileUploadRepository fileUploadRepository;
	protected ActivityPointRepository activityPointRepository;

	protected final static double latitudeMeter = 0.000009;
	protected final static double longitudeMeter = 0.000014;

	@Before
	public void setup() {

		routeRepository = mock(RouteRepository.class);
		fileUploadRepository = mock(FileUploadRepository.class);
		routePointRepository = mock(RoutePointRepository.class);
		activityRepository = mock(ActivityRepository.class);
		activityPointRepository = mock(ActivityPointRepository.class);

		allRoutes = new ArrayList<Route>();
		when(
		        routeRepository.findByStartLatitudeLessThanAndStartLongitudeLessThanAndStartLatitudeGreaterThanAndStartLongitudeGreaterThan(anyDouble(),
		                anyDouble(), anyDouble(), anyDouble())).thenReturn(allRoutes);

		when(routeRepository.findAll()).thenReturn(allRoutes);

		underTest = createService(activityRepository, activityPointRepository, routeRepository, routePointRepository, fileUploadRepository);
	}

	protected Route setupRoute(long id, String name, double... points) {
		Route route = new Route(name);
		allRoutes.add(route);
		when(routeRepository.findOne(id)).thenReturn(route);
		List<RoutePoint> routePoints = new ArrayList<RoutePoint>();
		int i = 0;
		while (i < points.length) {
			RoutePoint routePoint = new RoutePoint(i / 2, points[i], points[i + 1], i);
			routePoint.setRoute(id);
			routePoints.add(routePoint);
			i += 2;
		}
		when(routePointRepository.findByRouteId(anyLong())).thenReturn(routePoints);
		return route;

	}

	protected abstract T createService(ActivityRepository activityRepository, ActivityPointRepository activityPointRepository, RouteRepository routeRepository,
	        RoutePointRepository routePointRepository, FileUploadRepository fileUploadRepository);

}
