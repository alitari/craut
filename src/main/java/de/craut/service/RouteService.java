package de.craut.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.craut.domain.Route;
import de.craut.domain.RouteActivity;
import de.craut.domain.RoutePoint;
import de.craut.domain.RoutePointRepository;
import de.craut.domain.RouteRepository;
import de.craut.util.geocalc.EarthCalc;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;

@Service
public class RouteService {

	private final RouteRepository routeRepository;
	private final RoutePointRepository routePointRepository;

	@Autowired
	public RouteService(RouteRepository routeRepository, RoutePointRepository routePointRepository) {
		this.routeRepository = routeRepository;
		this.routePointRepository = routePointRepository;
	}

	public Route getRoute(Long routeId) {
		return routeRepository.findOne(routeId);

	}

	public Route deleteRoute(Long routeId) {
		Route event = getRoute(routeId);
		routeRepository.delete(routeId);
		return event;
	}

	public List<Route> getAllRoutes() {
		Iterable<Route> findAll = routeRepository.findAll();
		List<Route> list = new ArrayList<Route>();
		for (Route item : findAll) {
			list.add(item);
		}
		return list;
	}

	public void createRoute(String name) {
		routeRepository.save(new Route(name, new Date()));
	}

	public void updateRoute(Route route) {
		routeRepository.save(route);
	}

	public List<RoutePoint> getRoutePoints(Route route) {
		return routePointRepository.findByRoute(route);
	}

	public double calcDistance(Route r1) {
		List<RoutePoint> routePoints = getRoutePoints(r1);
		RoutePoint first = routePoints.get(0);
		RoutePoint last = routePoints.get(routePoints.size() - 1);
		double distance = EarthCalc.getDistance(first, last);
		return distance;

	}

	public List<RouteActivity> createActivities(Iterator<GpxTrackPoint> gpxTrcks) {
		return null;
	}

}