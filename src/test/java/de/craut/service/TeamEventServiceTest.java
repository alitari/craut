package de.craut.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import de.craut.domain.TeamEvent;
import de.craut.domain.TeamEventRepository;
import de.craut.service.TeamEventService;

public class TeamEventServiceTest {

	private static final Long ID = 4364L;
	private static final String EVENTNAME = "Testaname";
	private static final Date STARTDATE = new Date();
	TeamEventService underTest;
	private TeamEventRepository teamEventRepository;
	private TeamEvent teamEvent;

	@Before
	public void setup() {
		teamEventRepository = mock(TeamEventRepository.class);
		teamEvent = new TeamEvent(EVENTNAME, STARTDATE);
		when(teamEventRepository.findOne(ID)).thenReturn(teamEvent);
		underTest = new TeamEventService(teamEventRepository);
	}

	@Test
	public void create() {
		String name = "Testname";
		underTest.createEvent(name);
		verify(teamEventRepository, times(1)).save(any(TeamEvent.class));
	}

	@Test
	public void delete() {
		TeamEvent deleteEvent = underTest.deleteEvent(ID);
		assertThat(deleteEvent, is(teamEvent));
		verify(teamEventRepository, times(1)).delete(ID);
	}

	@Test
	public void getEvent() {
		TeamEvent getEvent = underTest.getEvent(ID);
		assertThat(getEvent, is(teamEvent));
		verify(teamEventRepository, times(1)).findOne(ID);
	}

	@Test
	public void update() {
		TeamEvent event = new TeamEvent("zewgez", new Date());
		underTest.updateEvent(event);
		verify(teamEventRepository, times(1)).save(event);
	}

}
