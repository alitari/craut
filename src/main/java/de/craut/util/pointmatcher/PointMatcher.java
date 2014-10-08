package de.craut.util.pointmatcher;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.data.geo.Point;

import de.craut.domain.RoutePoint;
import de.craut.util.geocalc.EarthCalc;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;

public abstract class PointMatcher<G extends GpxTrackPoint, R extends RoutePoint, A> {

	private final static Logger logger = Logger.getLogger(PointMatcher.class);

	protected final List<G> gpxTrackpoints;
	protected final List<R> routepoints;
	protected final List<A> matchedPoints;

	protected double currentDistance = -1;
	protected int trackPointCounter = 1;
	protected int currentRoutePointPos;
	protected int currentGpxTrackPointPos;
	protected double currentTrackPointSpeed;

	protected boolean inMatch;

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
		currentTrackPointSpeed = 0;
		logger.debug("start Matching " + gpxTrackpoints.size() + " trackpoints to " + routepoints.size() + " routePoints.");

		while (currentGpxTrackPointPos < gpxTrackpoints.size() && currentRoutePointPos < routepoints.size()) {
			currentTrackPointSpeed = calculateTrackpointSpeed();
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

	private double calculateTrackpointSpeed() {
		if (currentGpxTrackPointPos == 0)
			return 0;
		GpxTrackPoint previousGpxTrackPoint = gpxTrackpoints.get(currentGpxTrackPointPos - 1);
		GpxTrackPoint currentGpxTrackPoint = gpxTrackpoints.get(currentGpxTrackPointPos);
		double distance = EarthCalc.getDistance(currentGpxTrackPoint, previousGpxTrackPoint);
		long time = currentGpxTrackPoint.time.getTime() - previousGpxTrackPoint.time.getTime();
		double timeHours = (double) time / (1000 * 60 * 60);
		double distanceInKm = distance / 1000;
		return distanceInKm / timeHours;
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

	public static abstract class First<G extends GpxTrackPoint, R extends RoutePoint, A> extends PointMatcher<G, R, A> {

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

	public static abstract class Closest<G extends GpxTrackPoint, R extends RoutePoint, A> extends PointMatcher<G, R, A> {

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

}
