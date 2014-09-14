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

import de.craut.domain.Route;
import de.craut.domain.RouteRepository;
import de.craut.service.RouteService;

public class RouteServiceTest {

	private static final Long ID = 4364L;
	private static final String EVENTNAME = "Testaname";
	private static final Date STARTDATE = new Date();
	RouteService underTest;
	private RouteRepository routeRepository;
	private Route route;

	@Before
	public void setup() {
		routeRepository = mock(RouteRepository.class);
		route = new Route(EVENTNAME, STARTDATE);
		when(routeRepository.findOne(ID)).thenReturn(route);
		underTest = new RouteService(routeRepository);
	}

	@Test
	public void create() {
		String name = "Testname";
		underTest.createRoute(name);
		verify(routeRepository, times(1)).save(any(Route.class));
	}

	@Test
	public void delete() {
		Route deleteEvent = underTest.deleteRoute(ID);
		assertThat(deleteEvent, is(route));
		verify(routeRepository, times(1)).delete(ID);
	}

	@Test
	public void getEvent() {
		Route getEvent = underTest.getRoute(ID);
		assertThat(getEvent, is(route));
		verify(routeRepository, times(1)).findOne(ID);
	}

	@Test
	public void update() {
		Route event = new Route("zewgez", new Date());
		underTest.updateRoute(event);
		verify(routeRepository, times(1)).save(event);
	}

}
