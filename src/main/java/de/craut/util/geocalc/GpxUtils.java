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
		Point routeFirst = trkPoints.get(0);
		Point routeLast = trkPoints.get(trkPoints.size() - 1);
		double routeDistance = EarthCalc.getDistance(routeFirst, routeLast);

		Iterator<? extends Point> trackPointIter = trkPoints.iterator();
		Point last = trackPointIter.next();
		double maxDistance = 0;
		double minDistance = Double.MAX_VALUE;

		int count = 1;
		while (trackPointIter.hasNext()) {
			Point gpxTrackPoint = trackPointIter.next();
			double distance = EarthCalc.getDistance(gpxTrackPoint.getX(), gpxTrackPoint.getY(), last.getX(), last.getY());
			maxDistance = Math.max(maxDistance, distance);
			if (distance == maxDistance) {
				logger.debug("Point:" + count + ":  New max distance " + maxDistance + "m with points: " + gpxTrackPoint + "," + last);
			}
			minDistance = Math.min(minDistance, distance);

			count++;
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
