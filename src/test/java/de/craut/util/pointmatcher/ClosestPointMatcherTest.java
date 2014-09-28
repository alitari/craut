package de.craut.util.pointmatcher;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Test;
import org.springframework.data.geo.Point;

import de.craut.util.pointmatcher.PointMatcher.Closest;

public class ClosestPointMatcherTest extends PointMatcherTest {

	public static class ClosestMatchTreshHold2 extends Closest<Point, Point, Point> {

		public ClosestMatchTreshHold2(List<Point> gpxPoints, List<Point> routepoints) {
			super(gpxPoints, routepoints);
		}

		@Override
		protected boolean isMatch() {
			return currentDistance < 2;
		}

		@Override
		protected Point createResult(Point gpxPoint, Point routePoint) {
			return gpxPoint;
		}

	}

	@Override
	protected PointMatcher<Point, Point, Point> createPointMatcher(List<Point> gpxTrackPoints2, List<Point> routepoints2) {
		return new ClosestMatchTreshHold2(gpxTrackPoints2, routepoints2);
	}

	@Test
	public void nomatch3on2firstThird() {
		gpxTrackPoints.add(new Point(0, 0));
		gpxTrackPoints.add(new Point(1, 0));
		gpxTrackPoints.add(new Point(1, 1));

		routepoints.add(new Point(1, 1));
		routepoints.add(new Point(1, 2));

		List<Point> result = underTest.start();
		assertThat(result.size(), is(0));
	}

	@Test
	public void nomatch3on2secoundThird() {
		gpxTrackPoints.add(new Point(1, 1));
		gpxTrackPoints.add(new Point(2, 1));
		gpxTrackPoints.add(new Point(3, 1));

		routepoints.add(new Point(3, 1));
		routepoints.add(new Point(4, 1));

		List<Point> result = underTest.start();
		assertThat(result.size(), is(0));
	}

	@Test
	public void match4on2secoundThird() {
		gpxTrackPoints.add(new Point(1, 1));
		gpxTrackPoints.add(new Point(2, 1));
		gpxTrackPoints.add(new Point(3, 1));
		gpxTrackPoints.add(new Point(5, 1));
		gpxTrackPoints.add(new Point(6, 1));

		routepoints.add(new Point(3, 1));
		routepoints.add(new Point(5, 1));

		List<Point> result = underTest.start();
		assertThat(result.get(0), is(gpxTrackPoints.get(2)));
		assertThat(result.get(1), is(gpxTrackPoints.get(3)));
	}
}
