package de.craut.util.pointmatcher;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.geo.Point;

import de.craut.util.pointmatcher.PointMatcher.First;

public abstract class PointMatcherTest {

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

	protected List<Point> gpxTrackPoints;
	protected List<Point> routepoints;
	protected PointMatcher<Point, Point, Point> underTest;

	@Before
	public void setup() {

		gpxTrackPoints = new ArrayList<Point>();
		routepoints = new ArrayList<Point>();
		underTest = createPointMatcher(gpxTrackPoints, routepoints);
		// new FirstMatchTreshHold2(gpxTrackPoints, routepoints);
	}

	protected abstract PointMatcher<Point, Point, Point> createPointMatcher(List<Point> gpxTrackPoints2, List<Point> routepoints2);

	@Test
	public void match1on1() {
		gpxTrackPoints.add(new Point(1, 1));
		routepoints.add(new Point(1, 1));
		List<Point> result = underTest.start();
		assertThat(result.get(0), is(gpxTrackPoints.get(0)));
	}

	@Test
	public void match2on1First() {
		gpxTrackPoints.add(new Point(1, 1));
		gpxTrackPoints.add(new Point(2, 1));
		routepoints.add(new Point(1, 1));
		List<Point> result = underTest.start();
		assertThat(result.get(0), is(gpxTrackPoints.get(0)));
	}

	@Test
	public void match2on1Second() {
		gpxTrackPoints.add(new Point(3, 1));
		gpxTrackPoints.add(new Point(2, 1));
		routepoints.add(new Point(1, 1));
		List<Point> result = underTest.start();
		assertThat(result.get(0), is(gpxTrackPoints.get(1)));
	}

	@Test
	public void nomatch2on1First() {
		gpxTrackPoints.add(new Point(3, 1));
		gpxTrackPoints.add(new Point(1, 3));
		routepoints.add(new Point(1, 1));
		List<Point> result = underTest.start();
		assertThat(result.isEmpty(), is(true));
	}

	@Test
	public void nomatch3on2() {
		gpxTrackPoints.add(new Point(1, 1));
		gpxTrackPoints.add(new Point(2, 1));
		gpxTrackPoints.add(new Point(2, 0));

		routepoints.add(new Point(3, 1));
		routepoints.add(new Point(4, 1));

		List<Point> result = underTest.start();
		assertThat(result.size(), is(0));
	}

}
