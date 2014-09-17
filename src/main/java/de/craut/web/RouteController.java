package de.craut.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.craut.domain.Route;
import de.craut.domain.RoutePoint;
import de.craut.service.RouteService;

@Controller
@RequestMapping("/routes")
@EnableAutoConfiguration
public class RouteController {

	@Autowired
	private RouteService routeService;

	@RequestMapping("/init")
	public String init(@RequestParam(value = "name", required = false, defaultValue = "you") String name, Model model) {
		model.addAttribute("name", name);
		fillPageContent(model);
		fillRoutes(model);
		return "routes";
	}

	private void fillPageContent(Model model) {
		model.addAttribute("menuPage", NavElement.MENU_PAGE);
		model.addAttribute("menuMain", NavElement.MENU_MAIN);
		model.addAttribute("blockContent", "Twitter Blog...");
		model.addAttribute("subscriptionContent", "subscriptionContent...");
		model.addAttribute("otherServicesContent", "otherServicesContent...");
	}

	@RequestMapping("/delete")
	public String delete(@RequestParam(value = "id", required = true) Long id, Model model) {
		fillPageContent(model);
		routeService.deleteRoute(id);
		fillRoutes(model);
		return "routes";
	}

	@RequestMapping("/create")
	public String create(Model model) {
		fillPageContent(model);
		return "route";
	}

	@RequestMapping("/created")
	public String created(@RequestParam(value = "name", required = true) String name, Model model) {
		fillPageContent(model);
		routeService.createRoute(name);
		fillRoutes(model);
		return "routes";
	}

	@RequestMapping("/edit")
	public String edit(@RequestParam(value = "id", required = true) Long id, Model model) {
		fillPageContent(model);
		Route route = routeService.getRoute(id);
		model.addAttribute("route", route);
		model.addAttribute("routePoints", Arrays.asList(new RoutePoint(route, "37.772323", "-122.214897"), new RoutePoint(route, "21.291982", "-157.821856"),
		        new RoutePoint(route, "-18.142599", "178.431"), new RoutePoint(route, "-27.46758", "153.027892")));
		model.addAttribute("script", "/routeDetailsScript.tpl");
		return "route";
	}

	@RequestMapping("/edited")
	public String edited(@RequestParam(value = "id", required = true) Long id, @RequestParam(value = "name", required = true) String name, Model model) {
		fillPageContent(model);
		Route route = routeService.getRoute(id);
		route.setName(name);
		routeService.updateRoute(route);
		fillRoutes(model);
		return "routes";
	}

	private void fillRoutes(Model model) {
		List<Route> routes = routeService.getAll();
		model.addAttribute("routes", routes);
	}

	// private Route deleteEvent(Long id) {
	// String url = "http://localhost:8090/events/delete/" + id;
	// RestTemplate eventRest = new RestTemplate();
	// Route deletedRoute = eventRest.postForObject(url, null,
	// Route.class);
	// return deletedRoute;
	// }

	// private List<Route> getEventsRest() {
	// RestTemplate eventRest = new RestTemplate();
	// List<Route> events = (List<Route>)
	// eventRest.getForObject("http://localhost:8090/events", List.class);
	// return events;
	// }

}