package de.craut.web;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import de.craut.domain.Route;
import de.craut.domain.RoutePoint;

@Controller
@RequestMapping("/routes")
@EnableAutoConfiguration
public class RouteController extends AbstractController {

	@RequestMapping("/list")
	public String list(Model model) {
		fillPageContent(model);
		return "routes";
	}

	private void fillPageContent(Model model) {
		fillPageContent(model, "routes");

		List<Route> routes = routeService.getAllRoutes();
		model.addAttribute("routes", routes);
	}

	@RequestMapping("/delete")
	public String delete(@RequestParam(value = "id", required = true) Long id, Model model) {
		routeService.deleteRoute(id);
		fillPageContent(model);
		return "routes";
	}

	@RequestMapping("/create")
	public String create(Model model) {
		fillPageContent(model);
		return "route";
	}

	@RequestMapping("/created")
	public String created(@RequestParam(value = "name", required = true) String name, Model model) {
		routeService.createRoute(name);
		fillPageContent(model);
		return "routes";
	}

	@RequestMapping("/edit")
	public String edit(@RequestParam(value = "id", required = true) Long id, Model model) {
		fillPageContent(model);
		fillRoute(model, id);
		return "route";
	}

	@RequestMapping("/edited")
	public String edited(@RequestParam(value = "id", required = true) Long id, @RequestParam(value = "name", required = true) String name, Model model) {
		Route route = routeService.getRoute(id);
		route.setName(name);
		routeService.updateRoute(route);
		fillPageContent(model);
		return "routes";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public @ResponseBody String upload() {
		return "You can upload a file by posting to this same URL.";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(@RequestParam("file") MultipartFile file, Model model) {
		uploadFile(file, model);
		fillPageContent(model);
		return "routes";
	}

	private void fillRoute(Model model, Long id) {
		Route route = routeService.getRoute(id);

		List<RoutePoint> routePoints = routeService.getRoutePoints(route);

		model.addAttribute("route", route);
		List<Double> routePointsLatLng = fillLatLng(routePoints);
		model.addAttribute("routePoints", routePointsLatLng);

		double calcDistance = routeService.calcDistance(route);
		model.addAttribute("distance", new DecimalFormat("###,##").format(calcDistance));
	}

	private List<Double> fillLatLng(List<RoutePoint> routePoints) {
		List<Double> latLngs = new ArrayList<Double>();
		for (RoutePoint routePoint : routePoints) {
			latLngs.add(routePoint.getLatitude());
			latLngs.add(routePoint.getLongitude());
		}
		return latLngs;
	}
}