package de.craut.util.geocalc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.geo.Point;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class GPXParser {

	private final static Logger logger = Logger.getLogger("GPXParser");

	private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");

	private SAXParser saxParser;
	private List<GpxTrackPoint> trackPoints;

	public static class GpxTrackPoint extends Point {
		public final Date time;
		public final int elevation;
		public final int heartRate;
		public final int power;
		public final int cadence;
		public final double temperature;

		public GpxTrackPoint(double latitude, double longitude) {
			this(latitude, longitude, new Date(), 1, 0, 0, 0, 0);
		}

		public GpxTrackPoint(double latitude, double longitude, Date time, int elevation, int heartRate, int power, int cadence, double temperature) {
			super(latitude, longitude);
			this.time = time;
			this.elevation = elevation;
			this.heartRate = heartRate;
			this.power = power;
			this.cadence = cadence;
			this.temperature = temperature;
		}

		public double getLatitude() {
			return getX();
		}

		public double getLongitude() {
			return getY();
		}

		@Override
		public String toString() {
			return "GpxTrackPoint [latitude=" + getX() + ", longitude=" + getY() + ", elevation=" + elevation + "]";
		}

	}

	public GPXParser() {
		super();
		trackPoints = new ArrayList<GpxTrackPoint>();
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		try {
			saxParser = spf.newSAXParser();
		} catch (ParserConfigurationException e) {
			throw new ParsingException("Unable to create Parser", e);
		} catch (SAXException e) {
			throw new ParsingException("Unable to create Parser", e);
		}

	}

	public List<GpxTrackPoint> parse(InputStream is) {

		try {
			XMLReader xmlReader = saxParser.getXMLReader();
			xmlReader.setContentHandler(new SAXLocalNameCount());
			xmlReader.setErrorHandler(new MyErrorHandler());
			xmlReader.parse(new InputSource(is));
		} catch (SAXException e) {
			throw new ParsingException("Unable to Parse", e);
		} catch (FileNotFoundException e) {
			throw new ParsingException("File not found", e);
		} catch (IOException e) {
			throw new ParsingException("Problem reading ", e);
		}

		return trackPoints;

	}

	public class SAXLocalNameCount extends DefaultHandler {

		String latitude = "";
		String longitude = "";
		String timeStr = "";
		String elevation = "";
		String heartRate = "";
		String cadence = "";
		String power = "";

		@Override
		public void startDocument() throws SAXException {

		}

		@Override
		public void endDocument() throws SAXException {

		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

			if (localName.equals("trkpt")) {
				latitude = attributes.getValue("lat");
				longitude = attributes.getValue("lon");

			} else if (localName.equals("time")) {
				timeStr = "ready";
			} else if (localName.equals("ele")) {
				elevation = "ready";
			} else if (localName.equals("hr")) {
				heartRate = "ready";
			} else if (localName.equals("cad")) {
				cadence = "ready";
			} else if (localName.equals("power")) {
				power = "ready";
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			char[] copyOfRange = Arrays.copyOfRange(ch, start, start + length);
			if (timeStr.equals("ready")) {
				timeStr = new String(copyOfRange);
			} else if (elevation.equals("ready")) {
				elevation = new String(copyOfRange);
			} else if (heartRate.equals("ready")) {
				heartRate = new String(copyOfRange);
			} else if (cadence.equals("ready")) {
				cadence = new String(copyOfRange);
			} else if (power.equals("ready")) {
				power = new String(copyOfRange);
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (localName.equals("trkpt")) {
				Date time = parseTime(timeStr);
				String eleCut = StringUtils.substringBefore(elevation, ".");
				int ele = StringUtils.isNumeric(eleCut) ? Integer.parseInt(eleCut) : 0;
				int hr = StringUtils.isNumeric(heartRate) ? Integer.parseInt(heartRate) : 0;
				int pow = StringUtils.isNumeric(power) ? Integer.parseInt(power) : 0;
				int cad = StringUtils.isNumeric(cadence) ? Integer.parseInt(cadence) : 0;

				GpxTrackPoint routePoint = new GpxTrackPoint(Double.parseDouble(latitude), Double.parseDouble(longitude), time, ele, hr, pow, cad, 0);
				trackPoints.add(routePoint);
			}
		}
	}

	private static class MyErrorHandler implements ErrorHandler {

		MyErrorHandler() {
		}

		private String getParseExceptionInfo(SAXParseException spe) {
			String systemId = spe.getSystemId();

			if (systemId == null) {
				systemId = "null";
			}

			String info = "URI=" + systemId + " Line=" + spe.getLineNumber() + ": " + spe.getMessage();

			return info;
		}

		@Override
		public void warning(SAXParseException spe) throws SAXException {
			System.out.println("Warning: " + getParseExceptionInfo(spe));
		}

		@Override
		public void error(SAXParseException spe) throws SAXException {
			String message = "Error: " + getParseExceptionInfo(spe);
			throw new SAXException(message);
		}

		@Override
		public void fatalError(SAXParseException spe) throws SAXException {
			String message = "Fatal Error: " + getParseExceptionInfo(spe);
			throw new SAXException(message);
		}
	}

	public static class ParsingException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public ParsingException(String message, Throwable e) {
			super(message, e);
		}
	}

	Date parseTime(String dateStr) {
		if (StringUtils.isEmpty(dateStr)) {
			return null;
		}
		Date date;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			throw new ParsingException("Can't parse time", e);
		}

		return date;
	}
}