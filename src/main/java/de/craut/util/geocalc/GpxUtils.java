package de.craut.util.geocalc;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.data.geo.Point;

import de.craut.util.geocalc.GPXParser.GpxTrackPoint;

public class GpxUtils {

	private static final Logger logger = Logger.getLogger(GpxUtils.class);

	public static GpxPointStatistics getStatistics(List<? extends Point> trkPoints) {
		double routeDistance = 0;

		Iterator<? extends Point> trackPointIter = trkPoints.iterator();
		Point last = trackPointIter.next();
		double maxDistance = 0;
		double minDistance = Double.MAX_VALUE;

		while (trackPointIter.hasNext()) {
			Point gpxTrackPoint = trackPointIter.next();
			double distance = EarthCalc.getDistance(gpxTrackPoint.getX(), gpxTrackPoint.getY(), last.getX(), last.getY());
			routeDistance += distance;
			maxDistance = Math.max(maxDistance, distance);
			minDistance = Math.min(minDistance, distance);
			last = gpxTrackPoint;
		}

		return new GpxPointStatistics(trkPoints.size(), routeDistance, minDistance, maxDistance);

	}

	public static List<GpxTrackPoint> gpxFromFile(String resourcePath) {
		InputStream gpxIs = GpxUtils.class.getResourceAsStream(resourcePath); //
		GPXParser gpxParser = new GPXParser();
		List<GpxTrackPoint> gpxPoints = gpxParser.parse(gpxIs);
		return gpxPoints;
	}

}
