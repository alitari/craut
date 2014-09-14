package de.craut.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.craut.domain.Route;
import de.craut.service.RouteService;

@RestController
@RequestMapping(value = "/rest/routes")
public class RouteRestController {

	@Autowired
	RouteService routeService;

	public RouteRestController() {
	}

	@RequestMapping(value = "/{eventId}", method = RequestMethod.GET)
	public Route getEvent(@PathVariable Long eventId) {
		return routeService.getRoute(eventId);
	}

	@RequestMapping(value = "/delete/{eventId}", method = RequestMethod.POST)
	public Route deleteEvent(@PathVariable Long eventId) {
		return routeService.deleteRoute(eventId);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<Route> getAll() {
		return routeService.getAll();
	}

}