package de.craut.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.craut.domain.Activity;
import de.craut.domain.ActivityPoint;
import de.craut.domain.ActivityRepository;
import de.craut.domain.Route;
import de.craut.domain.RoutePoint;
import de.craut.domain.RoutePointRepository;
import de.craut.domain.RouteRepository;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;
import de.craut.util.geocalc.GpxPointStatistics;
import de.craut.util.geocalc.GpxUtils;
import de.craut.util.pointmatcher.PointMatcher;
import de.craut.util.pointmatcher.PointMatcher.ActivityMatchTreshHold20;

@Service
public class ActivityService {

	private final ActivityRepository activityRepository;
	private final RouteRepository routeRepository;
	private final RoutePointRepository routePointRepository;

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	public ActivityService(ActivityRepository activityRepository, RouteRepository routeRepository, RoutePointRepository routePointRepository) {
		this.activityRepository = activityRepository;
		this.routeRepository = routeRepository;
		this.routePointRepository = routePointRepository;
	}

	public List<Activity> fetchAllActivities() {
		List<Activity> activities = new ArrayList<Activity>();
		CollectionUtils.addAll(activities, activityRepository.findAll().iterator());
		return activities;

	}

	public Map<Activity, List<ActivityPoint>> createActivities(List<GpxTrackPoint> gpxPoints) {
		Map<Activity, List<ActivityPoint>> activityMap = new HashMap<Activity, List<ActivityPoint>>();

		GpxPointStatistics statistics = GpxUtils.getStatistics(gpxPoints);

		Iterator<Route> routeIter = routeRepository.findAll().iterator();// TODO:
																		 // filter

		logger.info("create Activities for gpxTrackPoints. " + "statistics=" + statistics + ",  checking routes, for matching... ");

		while (routeIter.hasNext()) {
			Route route = routeIter.next();
			List<RoutePoint> routepoints = routePointRepository.findByRoute(route);
			List<ActivityPoint> activityPoints = new PointMatcher.ActivityMatchTreshHold20(gpxPoints, routepoints).start();
			if (!activityPoints.isEmpty()) {
				logger.info("TrackPoints matched to " + route + ", creating Activity...");
				activityMap.put(activityPoints.get(0).getActivity(), activityPoints);
			}
		}

		return activityMap;
	}

	List<ActivityPoint> createActivityPoints2(List<GpxTrackPoint> gpxPoints, Route route) {
		List<RoutePoint> routepoints = routePointRepository.findByRoute(route);
		ActivityMatchTreshHold20 pointMatcher = new PointMatcher.ActivityMatchTreshHold20(gpxPoints, routepoints);
		return pointMatcher.start();
	}

}