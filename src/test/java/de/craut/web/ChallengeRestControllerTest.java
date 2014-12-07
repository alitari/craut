package de.craut.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import de.craut.TestContext;
import de.craut.WebConfig;
import de.craut.domain.Activity;
import de.craut.domain.Challenge;
import de.craut.domain.Route;
import de.craut.domain.User;
import de.craut.service.ChallengeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContext.class, WebConfig.class })
@WebAppConfiguration
@ActiveProfiles("test")
public class ChallengeRestControllerTest {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(),
	        Charset.forName("utf8"));

	private MockMvc mockMvc;

	@Autowired
	private ChallengeService challengeService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Before
	public void setup() {
		Mockito.reset(challengeService);
		HashMap<Challenge, List<Activity>> challenges = new HashMap<Challenge, List<Activity>>();
		Challenge challenge = new Challenge("Rote Lache");
		Route route = createRoute("Rote Lache", 110, 100, 5, 6);
		challenge.getRoutes().add(route);
		User user = new User("user1", "password1");
		Activity activity = new Activity("activity1", user, route, 10000, 30000);

		List<Activity> activitiesRoteLache = new ArrayList<Activity>();
		activitiesRoteLache.add(activity);

		challenges.put(challenge, activitiesRoteLache);

		when(challengeService.fetchAllChallenges()).thenReturn(challenges);
		mockMvc = webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void getChallenges() throws Exception {
		String expect = "[{\"activities\":[{\"id\":0,\"user\":{\"id\":0,\"name\":\"user1\",\"password\":\"password1\"},\"route\":{\"id\":0,\"name\":\"Rote Lache\",\"startLatitude\":5.0,\"startLongitude\":6.0,\"endLatitude\":5.001,\"endLongitude\":6.001,\"distance\":110.0,\"elevation\":100},\"name\":\"activity1\",\"start\":10000,\"end\":30000,\"powerAverage\":0.0,\"powerMax\":0,\"powerMin\":0,\"cadenceAverage\":0.0,\"cadenceMax\":0,\"cadenceMin\":0,\"speedAverage\":0.0,\"speedMax\":0.0,\"speedMin\":0.0,\"heartRateAverage\":0.0,\"heartRateMax\":0,\"heartRateMin\":0}],\"id\":0,\"routes\":[{\"id\":0,\"name\":\"Rote Lache\",\"startLatitude\":5.0,\"startLongitude\":6.0,\"endLatitude\":5.001,\"endLongitude\":6.001,\"distance\":110.0,\"elevation\":100}]}]";
		mockMvc.perform(get("/rest/challenges").contentType(APPLICATION_JSON_UTF8).content("content")).andExpect(status().isOk())
		        .andExpect(content().contentType(APPLICATION_JSON_UTF8)).andExpect(content().string(expect));
	}

	private Route createRoute(String name, double distance, int elevation, double startLatitude, double startLongitude) {
		Route route = new Route(name);
		route.setDistance(distance);
		route.setElevation(elevation);
		route.setStartLatitude(startLatitude);
		route.setStartLongitude(startLongitude);
		route.setEndLatitude(startLatitude + 0.001);
		route.setEndLongitude(startLongitude + 0.001);
		return route;
	}

}
