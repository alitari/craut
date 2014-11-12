package de.craut.web;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Page;
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

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(@RequestParam(value = "page", required = false) Integer pageNumber, Model model) {
		fillPageContent(model, pageNumber == null || pageNumber < 0 ? 0 : pageNumber);
		return "routes";
	}

	private void fillPageContent(Model model, int pageNumber) {
		Page<Route> routePage = routeService.fetchRoutes(pageNumber);
		fillPageContent(model, "routes", routePage);
	}

	@RequestMapping("/delete")
	public String delete(@RequestParam(value = "id", required = true) Long id, Model model) {
		routeService.deleteRoute(id);
		fillPageContent(model, 0);
		return "routes";
	}

	@RequestMapping("/create")
	public String create(Model model) {
		fillPageContent(model, 0);
		return "route";
	}

	@RequestMapping("/edit")
	public String edit(@RequestParam(value = "id", required = true) Long id, Model model) {
		fillPageContent(model, 0);
		fillRoute(model, id);
		return "route";
	}

	@RequestMapping("/edited")
	public String edited(@RequestParam(value = "id", required = true) Long id, @RequestParam(value = "name", required = true) String name, Model model) {
		Route route = routeService.fetchRoute(id);
		route.setName(name);
		routeService.updateRoute(route);
		fillPageContent(model, 0);
		return "routes";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public @ResponseBody String upload() {
		return "You can upload a file by posting to this same URL.";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(@RequestParam("file") MultipartFile file, @RequestParam("name") String name, Model model) {
		if (StringUtils.isEmpty(name)) {
			name = file.getOriginalFilename() + "," + System.currentTimeMillis();
		}
		List<GpxTrackPoint> trkPoints = parseGpxFile(file);

		Route saveRoute = routeService.saveRoute(name, trkPoints);
		if (saveRoute == null) {
			model.addAttribute("uploadMessage", "Route data not correct.");
		}

		fillPageContent(model, 0);
		return "routes";
	}

	private void fillRoute(Model model, Long id) {
		Route route = routeService.fetchRoute(id);

		List<RoutePoint> routePoints = routeService.fetchRoutePoints(route);
		GpxPointStatistics gpxStatistics = GpxUtils.getStatistics(routePoints);
		model.addAttribute("gpxStatistics", gpxStatistics);

		model.addAttribute("route", route);
		model.addAttribute("routePoints", routePoints);

	}
}