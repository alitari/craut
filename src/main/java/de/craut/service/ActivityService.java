package de.craut.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.craut.domain.ActivityRepository;
import de.craut.domain.Activity;

@Service
public class ActivityService {

	private final ActivityRepository activityRepository;

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	public ActivityService(ActivityRepository activityRepository) {
		this.activityRepository = activityRepository;
	}

	public List<Activity> fetchAllActivities() {
		List<Activity> activities = new ArrayList<Activity>();
		CollectionUtils.addAll(activities, activityRepository.findAll().iterator());
		return activities;

	}
}