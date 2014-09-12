package de.craut.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.craut.domain.TeamEvent;
import de.craut.service.TeamEventService;

@RestController
@RequestMapping(value = "/rest/teamevents")
public class TeamEventRestController {

	@Autowired
	TeamEventService teamEventService;

	public TeamEventRestController() {
	}

	@RequestMapping(value = "/{eventId}", method = RequestMethod.GET)
	public TeamEvent getEvent(@PathVariable Long eventId) {
		return teamEventService.getEvent(eventId);
	}

	@RequestMapping(value = "/delete/{eventId}", method = RequestMethod.POST)
	public TeamEvent deleteEvent(@PathVariable Long eventId) {
		return teamEventService.deleteEvent(eventId);
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<TeamEvent> getAll() {
		return teamEventService.getAll();
	}

}