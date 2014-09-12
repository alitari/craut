package de.craut.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.craut.domain.TeamEvent;
import de.craut.service.TeamEventService;

@Controller
@RequestMapping("/teamevents")
@EnableAutoConfiguration
public class TeamEventController {

	@Autowired
	private TeamEventService teamEventService;

	@RequestMapping("/init")
	public String init(@RequestParam(value = "name", required = false, defaultValue = "you") String name, Model model) {
		model.addAttribute("name", name);
		fillPageContent(model);
		fillEvents(model);
		return "teamevents";
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
		teamEventService.deleteEvent(id);
		fillEvents(model);
		return "teamevents";
	}

	@RequestMapping("/create")
	public String create(Model model) {
		fillPageContent(model);
		return "teamevent";
	}

	@RequestMapping("/created")
	public String created(@RequestParam(value = "name", required = true) String name, Model model) {
		fillPageContent(model);
		teamEventService.createEvent(name);
		fillEvents(model);
		return "teamevents";
	}

	@RequestMapping("/edit")
	public String edit(@RequestParam(value = "id", required = true) Long id, Model model) {
		fillPageContent(model);
		TeamEvent event = teamEventService.getEvent(id);
		model.addAttribute("event", event);
		return "teamevent";
	}

	@RequestMapping("/edited")
	public String edited(@RequestParam(value = "id", required = true) Long id, @RequestParam(value = "name", required = true) String name, Model model) {
		fillPageContent(model);
		TeamEvent event = teamEventService.getEvent(id);
		event.setName(name);
		teamEventService.updateEvent(event);
		fillEvents(model);
		return "teamevents";
	}

	private void fillEvents(Model model) {
		List<TeamEvent> events = teamEventService.getAll();
		model.addAttribute("events", events);
	}

	// private TeamEvent deleteEvent(Long id) {
	// String url = "http://localhost:8090/events/delete/" + id;
	// RestTemplate eventRest = new RestTemplate();
	// TeamEvent deletedTeamEvent = eventRest.postForObject(url, null,
	// TeamEvent.class);
	// return deletedTeamEvent;
	// }

	// private List<TeamEvent> getEventsRest() {
	// RestTemplate eventRest = new RestTemplate();
	// List<TeamEvent> events = (List<TeamEvent>)
	// eventRest.getForObject("http://localhost:8090/events", List.class);
	// return events;
	// }

}