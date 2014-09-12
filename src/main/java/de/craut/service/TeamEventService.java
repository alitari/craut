package de.craut.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.craut.domain.TeamEvent;
import de.craut.domain.TeamEventRepository;

@Service
public class TeamEventService {

	private final TeamEventRepository teamEventRepository;

	@Autowired
	public TeamEventService(TeamEventRepository teamEventRepository) {
		this.teamEventRepository = teamEventRepository;
	}

	public TeamEvent getEvent(Long eventId) {
		return teamEventRepository.findOne(eventId);

	}

	public TeamEvent deleteEvent(Long eventId) {
		TeamEvent event = getEvent(eventId);
		teamEventRepository.delete(eventId);
		return event;
	}

	public List<TeamEvent> getAll() {
		Iterable<TeamEvent> findAll = teamEventRepository.findAll();
		List<TeamEvent> list = new ArrayList<TeamEvent>();
		for (TeamEvent item : findAll) {
			list.add(item);
		}
		return list;
	}

	public void createEvent(String name) {
		teamEventRepository.save(new TeamEvent(name, new Date()));
	}

	public void updateEvent(TeamEvent event) {
		teamEventRepository.save(event);
	}

}