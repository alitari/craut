package de.craut.web;

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
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;
import de.craut.util.geocalc.GpxPointStatistics;
import de.craut.util.geocalc.GpxUtils;

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

		List<Route> routes = routeService.fetchAllRoutes();
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

	@RequestMapping("/edit")
	public String edit(@RequestParam(value = "id", required = true) Long id, Model model) {
		fillPageContent(model);
		fillRoute(model, id);
		return "route";
	}

	@RequestMapping("/edited")
	public String edited(@RequestParam(value = "id", required = true) Long id, @RequestParam(value = "name", required = true) String name, Model model) {
		Route route = routeService.fetchRoute(id);
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
	public String upload(@RequestParam("file") MultipartFile file, @RequestParam("name") String name, Model model) {
		List<GpxTrackPoint> trkPoints = parseGpxFile(file);
		GpxPointStatistics statistics = GpxUtils.getStatistics(trkPoints);
		String message = null;
		if (statistics.getMinDistance() < 5) {
			message = " min Point distance=" + statistics.getMinDistance();
		} else if (statistics.getRouteDistance() > 30000) {
			message = " route distance=" + statistics.getRouteDistance();
		} else if (statistics.getTrkPointCount() > 500) {
			message = " trackpointCount=" + statistics.getTrkPointCount();
		}

		if (message != null) {
			model.addAttribute("uploadMessage", "No routes matches.");
		} else {
			routeService.saveRoute(name, trkPoints);
		}

		fillPageContent(model);
		return "routes";
	}

	private void fillRoute(Model model, Long id) {
		Route route = routeService.fetchRoute(id);

		List<RoutePoint> routePoints = routeService.fetchRoutePoints(route);
		GpxPointStatistics gpxStatistics = GpxUtils.getStatistics(routePoints);
		model.addAttribute("gpxStatistics", gpxStatistics);

		model.addAttribute("route", route);
		List<Double> routePointsLatLng = fillLatLng(routePoints);
		model.addAttribute("routePoints", routePointsLatLng);

	}
}