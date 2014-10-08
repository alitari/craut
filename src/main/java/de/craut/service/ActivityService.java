package de.craut.service;

import java.util.ArrayList;
import java.util.Date;
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
import de.craut.util.geocalc.EarthCalc;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;
import de.craut.util.geocalc.GpxPointStatistics;
import de.craut.util.geocalc.GpxUtils;
import de.craut.util.pointmatcher.PointMatcher.Closest;

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

	public List<ActivityPoint> fetchActivityPoints(long id) {
		return activityPointRepository.findByActivityId(id);
	}

	public Map<Activity, List<ActivityPoint>> createActivities(List<GpxTrackPoint> gpxPoints) {
		Map<Activity, List<ActivityPoint>> activityMap = new HashMap<Activity, List<ActivityPoint>>();

		List<Route> routes = findRoutesCloseToPoint(gpxPoints);

		for (Route route : routes) {
			List<RoutePoint> routepoints = routePointRepository.findByRouteId(route.getId());
			List<ActivityPoint> activityPoints = new ActivityMatchTreshHold20(gpxPoints, routepoints).start();
			if (!activityPoints.isEmpty()) {
				logger.info("TrackPoints matched to " + route + ", creating Activity...");

				Activity activity = createActivityFromPoints(route, activityPoints);

				activityMap.put(activity, activityPoints);
			}
		}

		return activityMap;
	}

	Activity createActivityFromPoints(Route route, List<ActivityPoint> activityPoints) {
		ActivityPoint firstPoint = activityPoints.get(0);
		ActivityPoint lastPoint = activityPoints.get(activityPoints.size() - 1);
		long start = firstPoint.getTime();
		long end = lastPoint.getTime();

		Activity activity = new Activity(String.valueOf(new Date(start)), route, start, end);

		int hrMax = Integer.MIN_VALUE;
		int hrMin = Integer.MAX_VALUE;
		double hrAvg = 0;

		int cadMax = Integer.MIN_VALUE;
		int cadMin = Integer.MAX_VALUE;
		double cadAvg = 0;

		double speedMax = Double.MIN_VALUE;
		double speedMin = Double.MAX_VALUE;
		double speedAvg = 0;

		int powerMax = Integer.MIN_VALUE;
		int powerMin = Integer.MAX_VALUE;
		double powerAvg = 0;

		for (ActivityPoint activityPoint : activityPoints) {
			hrMax = Math.max(hrMax, activityPoint.getHeartRate());
			hrMin = Math.min(hrMin, activityPoint.getHeartRate());
			hrAvg += activityPoint.getHeartRate();

			cadMax = Math.max(cadMax, activityPoint.getCadence());
			cadMin = Math.min(cadMin, activityPoint.getCadence());
			cadAvg += activityPoint.getCadence();

			speedMax = Math.max(speedMax, activityPoint.getSpeed());
			speedMin = Math.min(speedMin, activityPoint.getSpeed());
			speedAvg += activityPoint.getSpeed();

			powerMax = Math.max(powerMax, activityPoint.getPower());
			powerMin = Math.min(powerMin, activityPoint.getPower());
			powerAvg += activityPoint.getPower();
		}
		hrAvg = hrAvg / activityPoints.size();
		speedAvg = speedAvg / activityPoints.size();
		cadAvg = cadAvg / activityPoints.size();
		powerAvg = powerAvg / activityPoints.size();

		activity.setCadenceAverage(cadAvg);
		activity.setCadenceMax(cadMax);
		activity.setCadenceMin(cadMin);
		activity.setHeartRateAverage(hrAvg);
		activity.setHeartRateMax(hrMax);
		activity.setHeartRateMin(hrMin);
		activity.setSpeedAverage(speedAvg);
		activity.setSpeedMax(speedMax);
		activity.setSpeedMin(speedMin);
		activity.setPowerAverage(powerAvg);
		activity.setPowerMax(powerMax);
		activity.setPowerMin(powerMin);
		return activity;
	}

	private List<Route> findRoutesCloseToPoint(List<GpxTrackPoint> gpxPoints) {
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
		return routes;
	}

	public Activity saveActivity(Activity activity, List<ActivityPoint> activityPoints) {
		Activity savedActivity = activityRepository.save(activity);
		for (ActivityPoint activityPoint : activityPoints) {
			activityPoint.setActivityId(savedActivity.getId());
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

	public static class ActivityMatchTreshHold20 extends Closest<GpxTrackPoint, RoutePoint, ActivityPoint> {

		public ActivityMatchTreshHold20(List<GpxTrackPoint> gpxPoints, List<RoutePoint> routepoints) {
			super(gpxPoints, routepoints);
		}

		@Override
		protected double calculateDistance() {
			return EarthCalc.getDistance(gpxTrackpoints.get(currentGpxTrackPointPos), routepoints.get(currentRoutePointPos));
		}

		@Override
		protected ActivityPoint createResult(GpxTrackPoint gpxTrackPoint, RoutePoint routePoint) {
			ActivityPoint activityPoint = new ActivityPoint(routePoint, gpxTrackPoint.time, currentTrackPointSpeed, gpxTrackPoint.heartRate,
			        gpxTrackPoint.cadence, gpxTrackPoint.power);
			return activityPoint;
		}

		@Override
		protected boolean isMatch() {
			return currentDistance < 20;
		}

	}

	public Activity fetchActivity(Long id) {
		return activityRepository.findOne(id);
	}

}