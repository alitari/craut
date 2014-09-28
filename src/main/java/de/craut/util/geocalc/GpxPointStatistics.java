package de.craut.util.geocalc;

import org.apache.log4j.Logger;

public class GpxPointStatistics {

	private static final Logger logger = Logger.getLogger(GpxPointStatistics.class);

	private final int trkPointCount;
	private final double routeDistance;
	private final double minDistance;
	private final double maxDistance;

	public GpxPointStatistics(int trkCount, double routeDistance, double minDistance, double maxDistance) {
		super();
		this.trkPointCount = trkCount;
		this.routeDistance = routeDistance;
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
	}

	@Override
	public String toString() {
		return "GpxPointStatistics [trkPointCount=" + trkPointCount + ", minDistance=" + minDistance + ", maxDistance=" + maxDistance + "]";
	}

	public int getTrkPointCount() {
		return trkPointCount;
	}

	public double getMinDistance() {
		return minDistance;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public double getRouteDistance() {
		return routeDistance;
	}

}
