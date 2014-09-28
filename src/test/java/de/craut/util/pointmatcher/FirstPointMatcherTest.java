package de.craut.util.pointmatcher;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.data.geo.Point;

import de.craut.util.pointmatcher.PointMatcher.First;

public class FirstPointMatcherTest extends PointMatcherTest {

	public static class FirstMatchTreshHold2 extends First<Point, Point, Point> {

		public FirstMatchTreshHold2(List<Point> gpxPoints, List<Point> routepoints) {
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
		protected Point createResult(Point gpxPoint, Point routePoint) {
			return gpxPoint;
		}

	}

	@Override
	protected PointMatcher<Point, Point, Point> createPointMatcher(List<Point> gpxTrackPoints2, List<Point> routepoints2) {
		return new FirstMatchTreshHold2(gpxTrackPoints2, routepoints2);
	}

	@Test
	public void match3on2firstThird() {
		gpxTrackPoints.add(new Point(0, 0));
		gpxTrackPoints.add(new Point(1, 0));
		gpxTrackPoints.add(new Point(1, 1));

		routepoints.add(new Point(1, 1));
		routepoints.add(new Point(1, 2));

		List<Point> result = underTest.start();
		assertThat(result.get(0), is(gpxTrackPoints.get(0)));
		assertThat(result.get(1), is(gpxTrackPoints.get(2)));
	}

	@Test
	public void match3on2secoundThird() {
		gpxTrackPoints.add(new Point(1, 1));
		gpxTrackPoints.add(new Point(2, 1));
		gpxTrackPoints.add(new Point(3, 1));

		routepoints.add(new Point(3, 1));
		routepoints.add(new Point(4, 1));

		List<Point> result = underTest.start();
		assertThat(result.get(0), is(gpxTrackPoints.get(1)));
		assertThat(result.get(1), is(gpxTrackPoints.get(2)));
	}

}
