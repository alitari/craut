package de.craut.web;

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
		fillRoute(model, id);
		return "route";
	}

	private void fillRoute(Model model, Long id) {
		Route route = routeService.getRoute(id);

		List<RoutePoint> routePoints = routeService.getRoutePoints(route);

		model.addAttribute("route", route);
		model.addAttribute("routePoints", routePoints);

		double calcDistance = routeService.calcDistance(route);
		model.addAttribute("distance", calcDistance);
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
		List<Route> routes = routeService.getAllRoutes();
		model.addAttribute("routes", routes);
	}

}