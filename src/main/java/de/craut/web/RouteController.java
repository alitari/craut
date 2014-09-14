package de.craut.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.craut.domain.Route;
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
		fillEvents(model);
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
		fillEvents(model);
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
		fillEvents(model);
		return "routes";
	}

	@RequestMapping("/edit")
	public String edit(@RequestParam(value = "id", required = true) Long id, Model model) {
		fillPageContent(model);
		Route event = routeService.getRoute(id);
		model.addAttribute("event", event);
		return "route";
	}

	@RequestMapping("/edited")
	public String edited(@RequestParam(value = "id", required = true) Long id, @RequestParam(value = "name", required = true) String name, Model model) {
		fillPageContent(model);
		Route event = routeService.getRoute(id);
		event.setName(name);
		routeService.updateRoute(event);
		fillEvents(model);
		return "routes";
	}

	private void fillEvents(Model model) {
		List<Route> events = routeService.getAll();
		model.addAttribute("events", events);
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