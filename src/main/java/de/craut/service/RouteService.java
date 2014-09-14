package de.craut.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.craut.domain.Route;
import de.craut.domain.RouteRepository;

@Service
public class RouteService {

	private final RouteRepository routeRepository;

	@Autowired
	public RouteService(RouteRepository routeRepository) {
		this.routeRepository = routeRepository;
	}

	public Route getRoute(Long routeId) {
		return routeRepository.findOne(routeId);

	}

	public Route deleteRoute(Long routeId) {
		Route event = getRoute(routeId);
		routeRepository.delete(routeId);
		return event;
	}

	public List<Route> getAll() {
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

}