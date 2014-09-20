package de.craut.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.craut.domain.Activity;
import de.craut.domain.ActivityPoint;
import de.craut.domain.FileUpload;
import de.craut.domain.FileUploadRepository;
import de.craut.domain.Route;
import de.craut.domain.RoutePoint;
import de.craut.domain.RoutePointRepository;
import de.craut.domain.RouteRepository;
import de.craut.util.AdvancedDateFormat;
import de.craut.util.geocalc.EarthCalc;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;

@Service
public class RouteService {

	private final RouteRepository routeRepository;
	private final RoutePointRepository routePointRepository;
	private final FileUploadRepository fileUploadRepository;

	private final Logger logger = Logger.getLogger(getClass());
	private int MAX_DISTANCE_TRESHOLD = 5;

	public static class GpxStatistics {
		public final int trkPointCount;
		public final double minDistance;
		public final double maxDistance;
		public final double minTimeDistance;
		public final double maxTimeDistance;

		public GpxStatistics(int trkCount, double minDistance, double maxDistance, double minTimeDistance, double maxTimeDistance) {
			super();
			this.trkPointCount = trkCount;
			this.minDistance = minDistance;
			this.maxDistance = maxDistance;
			this.maxTimeDistance = maxTimeDistance;
			this.minTimeDistance = minTimeDistance;
		}

		@Override
		public String toString() {
			return "GpxStatistics [trkPointCount=" + trkPointCount + ", minDistance=" + minDistance + ", maxDistance=" + maxDistance + ", minTimeDistance="
			        + minTimeDistance + ", maxTimeDistance=" + maxTimeDistance + "]";
		}

	}

	@Autowired
	public RouteService(RouteRepository routeRepository, RoutePointRepository routePointRepository, FileUploadRepository fileUploadRepository) {
		this.routeRepository = routeRepository;
		this.routePointRepository = routePointRepository;
		this.fileUploadRepository = fileUploadRepository;
	}

	public Route getRoute(Long routeId) {
		return routeRepository.findOne(routeId);

	}

	public Route deleteRoute(Long routeId) {
		Route event = getRoute(routeId);
		routeRepository.delete(routeId);
		return event;
	}

	public List<Route> getAllRoutes() {
		List<Route> list = new ArrayList<Route>();
		CollectionUtils.addAll(list, routeRepository.findAll().iterator());
		return list;
	}

	public void createRoute(String name) {
		routeRepository.save(new Route(name, new Date()));
	}

	public void updateRoute(Route route) {
		routeRepository.save(route);
	}

	public List<RoutePoint> getRoutePoints(Route route) {
		return routePointRepository.findByRoute(route);
	}

	public double calcDistance(Route r1) {
		List<RoutePoint> routePoints = getRoutePoints(r1);
		RoutePoint first = routePoints.get(0);
		RoutePoint last = routePoints.get(routePoints.size() - 1);
		double distance = EarthCalc.getDistance(first, last);
		return distance;

	}

	GpxStatistics getStatistics(List<GpxTrackPoint> trkPoints) {

		Iterator<GpxTrackPoint> trackPointIter = trkPoints.iterator();
		GpxTrackPoint last = trackPointIter.next();
		double maxDistance = 0;
		double minDistance = Double.MAX_VALUE;
		double maxTimeDistance = 0;
		double minTimeDistance = Double.MAX_VALUE;

		int count = 1;
		while (trackPointIter.hasNext()) {
			GpxTrackPoint gpxTrackPoint = trackPointIter.next();
			double distance = EarthCalc.getDistance(gpxTrackPoint.latitude, gpxTrackPoint.longitude, last.latitude, last.longitude);
			double timeDistance = gpxTrackPoint.time.getTimeInMillis() - last.time.getTimeInMillis();
			maxDistance = Math.max(maxDistance, distance);
			if (distance == maxDistance) {
				logger.debug("Point:" + count + ":  New max distance " + maxDistance + "m with points: " + gpxTrackPoint + "," + last);
			}
			minDistance = Math.min(minDistance, distance);
			minTimeDistance = Math.min(minTimeDistance, timeDistance);

			maxTimeDistance = Math.max(maxTimeDistance, timeDistance);
			if (maxTimeDistance == timeDistance) {
				logger.debug("Point:" + count + ":  New max time distance " + maxTimeDistance + "m with points: " + time(gpxTrackPoint) + "," + time(last));
			}

			count++;
			last = gpxTrackPoint;
			AdvancedDateFormat d;
		}

		return new GpxStatistics(trkPoints.size(), minDistance, maxDistance, minTimeDistance, maxTimeDistance);

	}

	private String time(GpxTrackPoint gpxTrackPoint) {
		return FastDateFormat.getInstance("hh:mm:ss").format(gpxTrackPoint.time);
	}

	public Map<Activity, List<ActivityPoint>> createActivities(List<GpxTrackPoint> trkPoints) {
		Map<Activity, List<ActivityPoint>> activityMap = new HashMap<Activity, List<ActivityPoint>>();

		GpxStatistics statistics = getStatistics(trkPoints);
		// between 3 and 7
		double distanceTreshold = Math.max(3, Math.min(statistics.maxDistance / 2, 7));

		List<Route> routes = getAllRoutes(); // TODO: filter
		logger.info("create Activities for gpxTrackPoints. " + "statistics=" + statistics + ",  checking " + routes.size() + " routes, for matching... ");

		Iterator<Route> routeIter = routes.iterator();

		while (routeIter.hasNext()) {
			Route route = routeIter.next();
			List<ActivityPoint> activityPoints = createActivityPoints(trkPoints, route, distanceTreshold);
			if (!activityPoints.isEmpty()) {
				logger.info("TrackPoints matched to " + route + ", creating Activity...");
				activityMap.put(activityPoints.get(0).getActivity(), activityPoints);
			}
		}

		return activityMap;
	}

	List<ActivityPoint> createActivityPoints(List<GpxTrackPoint> asList, Route route, double distanceTreshold) {

		List<ActivityPoint> activityPoints = new ArrayList<ActivityPoint>();

		logger.debug(" try creating ActivityPoints for " + route + " with " + asList.size() + " GpxTrackPoints using distanceTrashold of " + distanceTreshold);
		Activity activity = new Activity("Activity " + route.getName(), route, new Date());

		Iterator<RoutePoint> routePointsIter = getRoutePoints(route).iterator();
		Iterator<GpxTrackPoint> trackPointIter = asList.iterator();
		GpxTrackPoint gpxTrackPoint = trackPointIter.next();
		RoutePoint routePoint = routePointsIter.next();
		int trackPointCounter = 0;
		while (gpxTrackPoint != null && routePoint != null) {
			logger.debug("trackpoint:" + trackPointCounter + ":  calculating distance routePoint =" + routePoint + ",gpxPoint=" + gpxTrackPoint + "...");
			double distance = EarthCalc.getDistance(gpxTrackPoint.latitude, gpxTrackPoint.longitude, routePoint.getLatitude(), routePoint.getLongitude());
			logger.debug("distance =" + distance);

			if (distance < distanceTreshold) {
				logger.debug(" -> match!!! creating activityPoint...");
				ActivityPoint activityPoint = new ActivityPoint(activity, routePoint, gpxTrackPoint.time.getTime());
				activityPoints.add(activityPoint);
				routePoint = routePointsIter.hasNext() ? routePointsIter.next() : null;
			}
			gpxTrackPoint = trackPointIter.hasNext() ? trackPointIter.next() : null;
			trackPointCounter++;
		}

		if (routePoint != null) {
			activityPoints.clear();
		}
		return activityPoints;
	}

	public FileUpload fileUpload(byte[] content) {
		String thread = String.valueOf(Thread.currentThread().getId()) + " " + Thread.currentThread().getName();
		Date timeStamp = new Date();
		FileUpload fileUpload = new FileUpload(thread, content.length, content, timeStamp);
		return fileUploadRepository.save(fileUpload);
	}

	public List<FileUpload> fetchFileUploadsFromToday() {
		Date fromToday = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		return fileUploadRepository.findByInsertDateGreaterThan(fromToday);
	}
}