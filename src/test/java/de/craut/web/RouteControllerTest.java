package de.craut.web;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import de.craut.TestContext;
import de.craut.WebConfig;
import de.craut.domain.Route;
import de.craut.service.RouteService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContext.class, WebConfig.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class RouteControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private RouteService routeService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() {
		// We have to reset our mock between tests because the mock objects
		// are managed by the Spring container. If we would not reset them,
		// stubbing and verified behavior would "leak" from one test to another.
		Mockito.reset(routeService);

		List<Route> routeList = new ArrayList<Route>();
		Page<Route> routePage = new PageImpl<Route>(routeList);
		when(routeService.fetchRoutes(anyInt())).thenReturn(routePage);

		mockMvc = webAppContextSetup(webApplicationContext).build();

	}

	@Test
	public void routesInit() throws Exception {
		mockMvc.perform(get("/routes/list")).andExpect(status().isOk()).andExpect(view().name("routes"));
		// .andExpect(model().attribute("subscriptionContent",
		// is("subscriptionContent...")));

	}
}
