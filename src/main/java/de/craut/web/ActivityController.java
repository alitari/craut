package de.craut.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import de.craut.domain.Activity;
import de.craut.service.ActivityService;

@Controller
@RequestMapping("/activities")
@EnableAutoConfiguration
public class ActivityController extends AbstractController {

	@Autowired
	private ActivityService activityService;

	@RequestMapping("/list")
	public String list(Model model) {
		fillPageContent(model);
		return "activities";
	}

	private void fillPageContent(Model model) {
		fillPageContent(model, "activities");
		List<Activity> activities = activityService.fetchAllActivities();
		model.addAttribute("activities", activities);
	}

}