package de.craut.util.geocalc;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import de.craut.util.geocalc.GPXParser.GpxTrackPoint;

public class GPXParserTest {

	@Test
	public void testName() throws Exception {
		InputStream gpxIs = getClass().getResourceAsStream("/gpx/activities/test1.gpx");

		GPXParser gpxParser = new GPXParser();
		List<GpxTrackPoint> points = gpxParser.parse(gpxIs);
		assertThat(points.size(), is(3891));

		assertThat(points.get(467).getLatitude(), is(48.802322));

		// for (int i = 100; i <= 600; i++) {
		// GpxTrackPoint point = points.get(i);
		// if (i % 10 == 0) {
		// System.out.println("new RoutePoint(route," + point.latitude + "," +
		// point.longitude + "),");
		// }
		// }

	}

	@Test
	public void dateFormat() throws Exception {
		String dateStr = "2014-06-27T11:03:12Z";
		Date date = new GPXParser().parseTime(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		assertThat(cal.get(Calendar.SECOND), is(12));
		assertThat(cal.get(Calendar.MINUTE), is(3));
		assertThat(cal.get(Calendar.HOUR), is(11));
		assertThat(cal.get(Calendar.DAY_OF_MONTH), is(27));
		assertThat(cal.get(Calendar.MONTH), is(5));
		assertThat(cal.get(Calendar.YEAR), is(2014));

	}

}
