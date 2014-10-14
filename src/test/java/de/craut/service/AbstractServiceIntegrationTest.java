package de.craut.service;

import java.util.List;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.craut.ServiceTestContext;
import de.craut.domain.Route;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;
import de.craut.util.geocalc.GpxUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { ServiceTestContext.class })
@ActiveProfiles("test")
public abstract class AbstractServiceIntegrationTest {

	@Autowired
	protected RouteService routeService;

	@Autowired
	protected ActivityService activityService;

	@Before
	public void setup() {
		routeService.routeRepository.deleteAll();
		routeService.routePointRepository.deleteAll();

	}

	protected Route saveRoute(String route) {
		String routePath = "/gpx/routes/" + route + ".gpx";
		List<GpxTrackPoint> gpxPointsRoute = GpxUtils.gpxFromFile(routePath);
		Route savedRoute = routeService.saveRoute(route, gpxPointsRoute);
		return savedRoute;
	}

}
