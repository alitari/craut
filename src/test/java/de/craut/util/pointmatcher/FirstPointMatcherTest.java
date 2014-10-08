package de.craut.util.pointmatcher;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.data.geo.Point;

import de.craut.domain.RoutePoint;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;
import de.craut.util.pointmatcher.PointMatcher.First;

public class FirstPointMatcherTest extends PointMatcherTest {

	public static class FirstMatchTreshHold2 extends First<GpxTrackPoint, RoutePoint, Point> {

		public FirstMatchTreshHold2(List<GpxTrackPoint> gpxPoints, List<RoutePoint> routepoints) {
			super(gpxPoints, routepoints);
		}

		@Override
		protected boolean isMatch() {
			return currentDistance < 2;
		}

		@Override
		protected void unMatchAction() {
			// TODO Auto-generated method stub

		}

		@Override
		protected Point createResult(GpxTrackPoint gpxPoint, RoutePoint routePoint) {
			return gpxPoint;
		}

	}

	@Override
	protected PointMatcher<GpxTrackPoint, RoutePoint, Point> createPointMatcher(List<GpxTrackPoint> gpxTrackPoints2, List<RoutePoint> routepoints2) {
		return new FirstMatchTreshHold2(gpxTrackPoints2, routepoints2);
	}

	@Test
	public void match3on2firstThird() {
		gpxTrackPoints.add(new GpxTrackPoint(0, 0));
		gpxTrackPoints.add(new GpxTrackPoint(1, 0));
		gpxTrackPoints.add(new GpxTrackPoint(1, 1));

		routepoints.add(new RoutePoint(1, 1));
		routepoints.add(new RoutePoint(1, 2));

		List<Point> result = underTest.start();
		assertThat(result.get(0), equalTo((Point) gpxTrackPoints.get(0)));
		assertThat(result.get(1), equalTo((Point) gpxTrackPoints.get(2)));
	}

	@Test
	public void match3on2secoundThird() {
		gpxTrackPoints.add(new GpxTrackPoint(1, 1));
		gpxTrackPoints.add(new GpxTrackPoint(2, 1));
		gpxTrackPoints.add(new GpxTrackPoint(3, 1));

		routepoints.add(new RoutePoint(3, 1));
		routepoints.add(new RoutePoint(4, 1));

		List<Point> result = underTest.start();
		assertThat(result.get(0), equalTo((Point) gpxTrackPoints.get(1)));
		assertThat(result.get(1), equalTo((Point) gpxTrackPoints.get(2)));
	}

}
