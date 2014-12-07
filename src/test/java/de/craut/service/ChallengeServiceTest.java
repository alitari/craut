package de.craut.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.craut.domain.Activity;
import de.craut.domain.ActivityPointRepository;
import de.craut.domain.ActivityRepository;
import de.craut.domain.Challenge;
import de.craut.domain.ChallengeRepository;
import de.craut.domain.FileUploadRepository;
import de.craut.domain.Route;
import de.craut.domain.RoutePointRepository;
import de.craut.domain.RouteRepository;
import de.craut.domain.User;

public class ChallengeServiceTest extends ServiceTestWithRepositoryMocks<ChallengeService> {

	@Before
	public void setup() {
		super.setup();

		when(challengeRepository.findAll()).thenReturn(allChallenges);

		Answer<List<Activity>> answer = new Answer<List<Activity>>() {

			@Override
			public List<Activity> answer(InvocationOnMock invocation) throws Throwable {
				final Route route = (Route) invocation.getArguments()[0];
				ArrayList<Activity> filtered = new ArrayList<Activity>(allActivities);
				CollectionUtils.filter(filtered, new Predicate() {

					@Override
					public boolean evaluate(Object object) {
						return ((Activity) object).getRoute().equals(route);
					}
				});
				return filtered;
			}
		};
		when(activityRepository.findByRoute(any(Route.class))).then(answer);
	}

	private void createAndRegisterActivity(Route route, User user) {
		Activity activity = new Activity("activity1", user, route, 10000, 30000);
		allActivities.add(activity);
	}

	private void createAndRegisterChallenge(String name, Route route) {
		Challenge challenge = new Challenge(name);
		challenge.getRoutes().add(route);
		allChallenges.add(challenge);
	}

	@Test
	public void fetchAllChallengesOneActivity() throws Exception {
		Route route = createRoute(110, 100, 5, 6);
		createAndRegisterChallenge("Challenge1", route);

		User user = new User("user1", "password1");
		createAndRegisterActivity(route, user);

		Map<Challenge, List<Activity>> challenges = underTest.fetchAllChallenges();
		verify(challengeRepository, times(1)).findAll();

		verify(activityRepository, times(1)).findByRoute(allChallenges.get(0).getRoutes().get(0));
		assertThat(challenges.size(), is(allChallenges.size()));
		assertThat(challenges.get(allChallenges.get(0)).get(0), is(allActivities.get(0)));
	}

	@Test
	public void fetchAllChallengesMoreActivities() throws Exception {
		Route route = createRoute(110, 100, 5, 6);
		createAndRegisterChallenge("Challenge1", route);

		User user1 = new User("user1", "password1");
		createAndRegisterActivity(route, user1);

		User user2 = new User("user2", "password2");
		createAndRegisterActivity(route, user2);

		Map<Challenge, List<Activity>> challenges = underTest.fetchAllChallenges();
		verify(challengeRepository, times(1)).findAll();
		verify(activityRepository, times(1)).findByRoute(allChallenges.get(0).getRoutes().get(0));

		assertThat(challenges.size(), is(allChallenges.size()));
		assertThat(challenges.get(allChallenges.get(0)).get(0), is(allActivities.get(0)));
		assertThat(challenges.get(allChallenges.get(0)).get(1), is(allActivities.get(1)));
	}

	private Route createRoute(double distance, int elevation, double startLatitude, double startLongitude) {
		Route route = new Route("route1");
		route.setDistance(distance);
		route.setElevation(elevation);
		route.setStartLatitude(startLatitude);
		route.setStartLongitude(startLongitude);
		route.setEndLatitude(startLatitude + 0.001);
		route.setEndLongitude(startLongitude + 0.001);
		return route;
	}

	@Override
	protected ChallengeService createService(ChallengeRepository challengeRepsoitory, ActivityRepository activityRepository,
	        ActivityPointRepository activityPointRepository, RouteRepository routeRepository, RoutePointRepository routePointRepository,
	        FileUploadRepository fileUploadRepository) {
		return new ChallengeService(challengeRepsoitory, activityRepository);
	}
}
