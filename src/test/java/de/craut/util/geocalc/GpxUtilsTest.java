package de.craut.util.geocalc;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.springframework.data.geo.Point;

public class GpxUtilsTest {

	@Test
	public void getStatistics() throws Exception {
		GpxPointStatistics statistics = GpxUtils.getStatistics(GpxUtils.gpxFromFile("/gpx/activities/test2.gpx"));
		assertThat(statistics.getMinDistance(), is(4.828659858923302));
		assertThat(statistics.getMaxDistance(), is(11.125689133761142));

	}

	@Test
	public void distances() throws Exception {
		Point point1 = new Point(48, 8);
		Point point2 = new Point(49, 8);
		double distance = EarthCalc.getDistance(point1, point2);
		assertThat((int) (distance / 1000), is(111));

		point1 = new Point(48, 8);
		point2 = new Point(48, 9);
		distance = EarthCalc.getDistance(point1, point2);
		assertThat((int) (distance / 1000), is(74));

		// 1m latitude
		point1 = new Point(48, 8);
		point2 = new Point(48.000009, 8);
		distance = EarthCalc.getDistance(point1, point2);
		assertThat(distance, is(1.0002069982284665));

		// 1m longitude
		point1 = new Point(48, 8);
		point2 = new Point(48, 8.000014);
		distance = EarthCalc.getDistance(point1, point2);
		assertThat(distance, is(1.0399657163295126));

	}

}
