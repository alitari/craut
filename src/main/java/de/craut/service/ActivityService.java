package de.craut.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.craut.domain.Activity;
import de.craut.domain.ActivityPoint;
import de.craut.domain.ActivityPointRepository;
import de.craut.domain.ActivityRepository;
import de.craut.domain.Route;
import de.craut.domain.RoutePoint;
import de.craut.domain.RoutePointRepository;
import de.craut.domain.RouteRepository;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;
import de.craut.util.geocalc.GpxPointStatistics;
import de.craut.util.geocalc.GpxUtils;
import de.craut.util.pointmatcher.PointMatcher;

@Service
public class ActivityService {

	protected final static double latitudeMeter = 0.000009;
	protected final static double longitudeMeter = 0.000014;

	private final ActivityRepository activityRepository;
	private final ActivityPointRepository activityPointRepository;
	private final RouteRepository routeRepository;
	private final RoutePointRepository routePointRepository;

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	public ActivityService(ActivityRepository activityRepository, ActivityPointRepository activityPointRepository, RouteRepository routeRepository,
	        RoutePointRepository routePointRepository) {
		this.activityRepository = activityRepository;
		this.activityPointRepository = activityPointRepository;
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

		double latitudeOffet = latitudeMeter * statistics.getRouteDistance();
		double longitudeOffet = longitudeMeter * statistics.getRouteDistance();

		double upperBoundLatitude = gpxPoints.get(0).getLatitude() + latitudeOffet;
		double upperBoundLongitude = gpxPoints.get(0).getLongitude() + longitudeOffet;
		double lowerBoundLatitude = gpxPoints.get(0).getLatitude() - latitudeOffet;
		double lowerBoundLongitude = gpxPoints.get(0).getLongitude() - longitudeOffet;

		List<Route> routes = routeRepository.findByStartLatitudeLessThanAndStartLongitudeLessThanAndStartLatitudeGreaterThanAndStartLongitudeGreaterThan(
		        upperBoundLatitude, upperBoundLongitude, lowerBoundLatitude, lowerBoundLongitude);

		logger.info("create Activities for gpxTrackPoints. " + "statistics=" + statistics + ",  checking " + routes.size() + " routes, for matching... ");

		for (Route route : routes) {
			List<RoutePoint> routepoints = routePointRepository.findByRoute(route);
			List<ActivityPoint> activityPoints = new PointMatcher.ActivityMatchTreshHold20(gpxPoints, routepoints).start();
			if (!activityPoints.isEmpty()) {
				logger.info("TrackPoints matched to " + route + ", creating Activity...");
				activityMap.put(activityPoints.get(0).getActivity(), activityPoints);
			}
		}

		return activityMap;
	}

	public Activity saveActivity(Activity activity, List<ActivityPoint> activityPoints) {
		Activity savedActivity = activityRepository.save(activity);
		for (ActivityPoint activityPoint : activityPoints) {
			activityPoint.setActivity(savedActivity);
		}
		activityPointRepository.save(activityPoints);
		return savedActivity;
	}

	public void saveActivities(Map<Activity, List<ActivityPoint>> activityMap) {
		Set<Entry<Activity, List<ActivityPoint>>> activities = activityMap.entrySet();
		for (Entry<Activity, List<ActivityPoint>> entry : activities) {
			saveActivity(entry.getKey(), entry.getValue());
		}
	}
}