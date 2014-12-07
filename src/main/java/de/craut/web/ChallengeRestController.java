package de.craut.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.craut.domain.Activity;
import de.craut.domain.Route;
import de.craut.service.ChallengeService;

@RestController
@RequestMapping(value = "/rest/challenges")
public class ChallengeRestController {

	@Autowired
	ChallengeService challengeService;

	private class Challenge {
		final de.craut.domain.Challenge challenge;
		List<Activity> activities;

		public Challenge(de.craut.domain.Challenge challenge, List<Activity> activities) {
			super();
			this.challenge = challenge;
			this.activities = activities;
		}

		public List<Route> getRoutes() {

			return challenge.getRoutes();
		}

		public long getId() {
			return challenge.getId();
		}

		public List<Activity> getActivities() {
			return activities;
		}

	}

	public ChallengeRestController() {
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<Challenge> getAll() {
		List<Challenge> challengesRest = new ArrayList<Challenge>();
		Map<de.craut.domain.Challenge, List<Activity>> challenges = challengeService.fetchAllChallenges();
		for (Map.Entry<de.craut.domain.Challenge, List<Activity>> entry : challenges.entrySet()) {
			challengesRest.add(new Challenge(entry.getKey(), entry.getValue()));
		}
		return challengesRest;

	}
}