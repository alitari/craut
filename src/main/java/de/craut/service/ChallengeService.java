package de.craut.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import de.craut.domain.Activity;
import de.craut.domain.ActivityRepository;
import de.craut.domain.Challenge;
import de.craut.domain.ChallengeRepository;
import de.craut.domain.Route;

@Service
public class ChallengeService {

	final ChallengeRepository challengeRepository;
	final ActivityRepository activityRepository;

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	public ChallengeService(ChallengeRepository challengeRepository, ActivityRepository activityRepository) {
		this.challengeRepository = challengeRepository;
		this.activityRepository = activityRepository;
	}

	public Challenge deleteChallenge(Long challengeId) {
		Challenge challenge = challengeRepository.findOne(challengeId);
		challengeRepository.delete(challengeId);
		return challenge;
	}

	public List<Challenge> fetchAllChallenges() {
		return challengeRepository.findAll();
	}

	public Page<Challenge> fetchChallenges(Integer pageNumber) {
		PageRequest pageRequest = new PageRequest(pageNumber, 10, Sort.Direction.DESC, "id");
		return challengeRepository.findAll(pageRequest);
	}

	public List<Activity> fetchResults(Long challengeId) {
		Challenge challenge = challengeRepository.findOne(challengeId);
		List<Activity> activities = activityRepository.findByRoute(challenge.getRoutes().get(0));
		Collections.sort(activities, new Comparator<Activity>() {

			@Override
			public int compare(Activity o1, Activity o2) {
				return new Long(o1.getTime()).compareTo(new Long(o2.getTime()));
			}
		});
		return activities;
	}

	public Challenge saveChallenge(String name, Route route) {
		return saveChallenge(name, Arrays.asList(new Route[] { route }));
	}

	public Challenge saveChallenge(String name, List<Route> routes) {
		Challenge challenge = new Challenge(name);
		challenge.getRoutes().addAll(routes);
		challengeRepository.save(challenge);
		return challenge;
	}

	public void updatechallenge(Challenge challenge) {
		challengeRepository.save(challenge);
	}

	public Challenge fetchChallenge(Long id) {
		return challengeRepository.findOne(id);
	}

	public void updateChallange(Challenge challenge) {
		challengeRepository.save(challenge);
	}

}