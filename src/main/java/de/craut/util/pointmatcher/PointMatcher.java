package de.craut.util.pointmatcher;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.data.geo.Point;

import de.craut.domain.Activity;
import de.craut.domain.ActivityPoint;
import de.craut.domain.Route;
import de.craut.domain.RoutePoint;
import de.craut.util.geocalc.EarthCalc;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;

public abstract class PointMatcher<G extends Point, R extends Point, A> {

	private final static Logger logger = Logger.getLogger(PointMatcher.class);

	protected final List<G> gpxTrackpoints;
	protected final List<R> routepoints;
	protected final List<A> matchedPoints;

	double currentDistance = -1;
	int trackPointCounter = 1;
	int currentRoutePointPos;
	int currentGpxTrackPointPos;

	boolean inMatch;

	public PointMatcher(List<G> gpxPoints, List<R> routepoints) {
		super();
		this.gpxTrackpoints = gpxPoints;
		this.routepoints = routepoints;
		matchedPoints = new ArrayList<A>();
	}

	protected double calculateDistance() {
		Point currentRoutePoint = routepoints.get(currentRoutePointPos);
		Point currentGpxTrackPoint = gpxTrackpoints.get(currentGpxTrackPointPos);
		double x = Math.abs(currentRoutePoint.getX() - currentGpxTrackPoint.getX());
		double y = Math.abs(currentRoutePoint.getY() - currentGpxTrackPoint.getY());
		double result = Math.sqrt(x * x + y * y);
		return result;
	}

	public List<A> start() {
		currentGpxTrackPointPos = 0;
		currentRoutePointPos = 0;
		logger.debug("start Matching " + gpxTrackpoints.size() + " trackpoints to " + routepoints.size() + " routePoints.");

		while (currentGpxTrackPointPos < gpxTrackpoints.size() && currentRoutePointPos < routepoints.size()) {
			currentDistance = calculateDistance();
			if (currentDistance < 100) {
				logger.debug("distance(" + currentGpxTrackPointPos + "," + currentRoutePointPos + ") =" + currentDistance);
			}
			inMatch = isMatch();
			if (inMatch) {
				matchAction();
			} else {
				unMatchAction();
			}
			allways();
		}
		finish();
		return matchedPoints;
	}

	protected abstract void allways();

	protected void finish() {
		if (currentRoutePointPos < routepoints.size()) {
			matchedPoints.clear();
			logger.debug("Not all Routepoints match -> no result");
		} else {
			logger.debug("All Routepoints matched!");
		}

	}

	protected abstract void unMatchAction();

	protected abstract void matchAction();

	protected abstract boolean isMatch();

	protected abstract A createResult(G gpxPoint, R routePoint);

	public static abstract class First<G extends Point, R extends Point, A> extends PointMatcher<G, R, A> {

		public First(List<G> gpxPoints, List<R> routepoints) {
			super(gpxPoints, routepoints);
		}

		@Override
		protected void allways() {
			currentGpxTrackPointPos++;
		}

		@Override
		protected void matchAction() {
			G gpxTrackPoint = gpxTrackpoints.get(currentGpxTrackPointPos);
			R routePoint = routepoints.get(currentRoutePointPos);
			matchedPoints.add(createResult(gpxTrackPoint, routePoint));
			currentRoutePointPos++;
			logger.debug("MATCH (" + currentGpxTrackPointPos + "," + currentRoutePointPos + ")");
		}

	}

	public static abstract class Closest<G extends Point, R extends Point, A> extends PointMatcher<G, R, A> {

		protected G bestCandidate;
		protected int bestCandidatePos = -1;

		protected double mindisstance;

		public Closest(List<G> gpxPoints, List<R> routepoints) {
			super(gpxPoints, routepoints);
			bestCandidate = null;
			mindisstance = Double.MAX_VALUE;
		}

		@Override
		protected void matchAction() {
			if (currentDistance < mindisstance) {
				mindisstance = currentDistance;
				bestCandidate = gpxTrackpoints.get(currentGpxTrackPointPos);
				bestCandidatePos = currentGpxTrackPointPos;
			}
		}

		@Override
		protected void unMatchAction() {
			if (bestCandidate != null) {
				matchedPoints.add(createResult(bestCandidate, routepoints.get(currentRoutePointPos)));
				currentRoutePointPos++;
				bestCandidate = null;
				mindisstance = Double.MAX_VALUE;
				currentGpxTrackPointPos = bestCandidatePos;
				bestCandidatePos = -1;
			}
		}

		@Override
		protected void allways() {
			currentGpxTrackPointPos++;
		}

		@Override
		protected void finish() {
			if (bestCandidate != null) {
				matchedPoints.add(createResult(bestCandidate, routepoints.get(currentRoutePointPos)));
				currentRoutePointPos++;
			}

			super.finish();
		}

	}

	public static class ActivityMatchTreshHold20 extends Closest<GpxTrackPoint, RoutePoint, ActivityPoint> {

		private Activity activity;

		public ActivityMatchTreshHold20(List<GpxTrackPoint> gpxPoints, List<RoutePoint> routepoints) {
			super(gpxPoints, routepoints);
			Route route = routepoints.get(0).getRoute();
			Date date = gpxPoints.get(0).time;
			activity = new Activity("Activity " + date + " " + route.getName(), route, null, null);
		}

		@Override
		protected double calculateDistance() {
			return EarthCalc.getDistance(gpxTrackpoints.get(currentGpxTrackPointPos), routepoints.get(currentRoutePointPos));
		}

		@Override
		protected ActivityPoint createResult(GpxTrackPoint gpxTrackPoint, RoutePoint routePoint) {
			ActivityPoint activityPoint = new ActivityPoint(activity, routePoint, gpxTrackPoint.time);
			return activityPoint;
		}

		@Override
		protected boolean isMatch() {
			return currentDistance < 20;
		}

	}

}
