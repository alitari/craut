package de.craut.util.pointmatcher;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.geo.Point;

import de.craut.domain.RoutePoint;
import de.craut.util.geocalc.GPXParser.GpxTrackPoint;
import de.craut.util.pointmatcher.PointMatcher.First;

public abstract class PointMatcherTest {

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

	protected List<GpxTrackPoint> gpxTrackPoints;
	protected List<RoutePoint> routepoints;
	protected PointMatcher<GpxTrackPoint, RoutePoint, Point> underTest;

	@Before
	public void setup() {

		gpxTrackPoints = new ArrayList<GpxTrackPoint>();
		routepoints = new ArrayList<RoutePoint>();
		underTest = createPointMatcher(gpxTrackPoints, routepoints);
		// new FirstMatchTreshHold2(gpxTrackPoints, routepoints);
	}

	protected abstract PointMatcher<GpxTrackPoint, RoutePoint, Point> createPointMatcher(List<GpxTrackPoint> gpxTrackPoints2, List<RoutePoint> routepoints2);

	@Test
	public void match1on1() {
		gpxTrackPoints.add(new GpxTrackPoint(1, 1));
		routepoints.add(new RoutePoint(1, 1));
		List<Point> result = underTest.start();
		assertThat(result.get(0), equalTo((Point) gpxTrackPoints.get(0)));
	}

	@Test
	public void match2on1First() {
		gpxTrackPoints.add(new GpxTrackPoint(1, 1));
		gpxTrackPoints.add(new GpxTrackPoint(2, 1));
		routepoints.add(new RoutePoint(1, 1));
		List<Point> result = underTest.start();
		assertThat(result.get(0), equalTo((Point) gpxTrackPoints.get(0)));
	}

	@Test
	public void match2on1Second() {
		gpxTrackPoints.add(new GpxTrackPoint(3, 1));
		gpxTrackPoints.add(new GpxTrackPoint(2, 1));
		routepoints.add(new RoutePoint(1, 1));
		List<Point> result = underTest.start();
		assertThat(result.get(0), equalTo((Point) gpxTrackPoints.get(1)));
	}

	@Test
	public void nomatch2on1First() {
		gpxTrackPoints.add(new GpxTrackPoint(3, 1));
		gpxTrackPoints.add(new GpxTrackPoint(1, 3));
		routepoints.add(new RoutePoint(1, 1));
		List<Point> result = underTest.start();
		assertThat(result.isEmpty(), is(true));
	}

	@Test
	public void nomatch3on2() {
		gpxTrackPoints.add(new GpxTrackPoint(1, 1));
		gpxTrackPoints.add(new GpxTrackPoint(2, 1));
		gpxTrackPoints.add(new GpxTrackPoint(2, 0));

		routepoints.add(new RoutePoint(3, 1));
		routepoints.add(new RoutePoint(4, 1));

		List<Point> result = underTest.start();
		assertThat(result.size(), is(0));
	}

}
