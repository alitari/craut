package de.craut.web;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import de.craut.domain.Activity;
import de.craut.domain.ActivityPoint;
import de.craut.domain.User;
import de.craut.service.ActivityService;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;

@Controller
@RequestMapping("/activities")
@SessionAttributes("activityMap")
@EnableAutoConfiguration
public class ActivityController extends AbstractController {

	@Autowired
	private ActivityService activityService;

	@RequestMapping("/list")
	public String list(@RequestParam(value = "forUser", required = false) boolean forUser, Model model) {

		fillActivitiesContent(forUser ? user : null, model);
		return "activities";
	}

	@RequestMapping("/delete")
	public String delete(@RequestParam(value = "id", required = true) Long id, Model model) {
		activityService.deleteAvtivity(id);
		fillActivitiesContent(user, model);
		return "activities";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public @ResponseBody String upload() {
		return "You can upload a file by posting to this same URL.";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(@RequestParam("file") MultipartFile file, Model model) {
		List<GpxTrackPoint> trkPoints = parseGpxFile(file);

		Map<Activity, List<ActivityPoint>> activitiesMap = activityService.createActivities(user, trkPoints);
		if (!activitiesMap.isEmpty()) {
			model.addAttribute("activityMap", activitiesMap);
			return "activityselection";
		} else {
			model.addAttribute("uploadMessage", "No routes matches.");
			fillActivitiesContent(user, model);
			return "activities";
		}
	}

	@RequestMapping("/uploadselected")
	public String uploadselected(@RequestParam(value = "activitysave", required = true) String activitiesToSave, Model model) {

		Map<Activity, List<ActivityPoint>> activityMap = (Map<Activity, List<ActivityPoint>>) model.asMap().get("activityMap");

		Set<Activity> keySet = activityMap.keySet();
		for (Activity activity : keySet) {
			if (!activitiesToSave.contains(activity.getRoute().getName())) {
				activityMap.remove(activity);
			}
		}
		activityService.saveActivities(activityMap);
		activityMap.clear();

		fillActivitiesContent(user, model);
		return "activities";
	}

	private void fillActivitiesContent(User user, Model model) {
		fillPageContent(model, "Activities");
		List<Activity> activities = user == null ? activityService.fetchAllActivities() : activityService.fetchAllActivities(user);
		model.addAttribute("activities", activities);
	}

	@RequestMapping("/edit")
	public String edit(@RequestParam(value = "id", required = true) Long id, Model model) {
		fillPageContent(model, "Activities");
		fillActivity(model, id);
		return "activity";
	}

	private void fillActivity(Model model, Long id) {
		// Route route = routeService.fetchRoute(id);
		List<ActivityPoint> activityPoints = activityService.fetchActivityPoints(id);
		Activity activity = activityService.fetchActivity(id);
		model.addAttribute("activityPoints", activityPoints);
		model.addAttribute("activity", activity);
	}

}