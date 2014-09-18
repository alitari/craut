package de.craut.util.geocalc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
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

	public static class GpxTrackPoint {
		public final double latitude;
		public final double longitude;
		public final Calendar time;
		public int elevation;

		public GpxTrackPoint(double latitude, double longitude, Calendar time, int elevation) {
			super();
			this.latitude = latitude;
			this.longitude = longitude;
			this.time = time;
			this.elevation = elevation;
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

		public void startDocument() throws SAXException {

		}

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
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			if (timeStr.equals("ready")) {
				timeStr = new String(Arrays.copyOfRange(ch, start, start + length));
			} else if (elevation.equals("ready")) {
				elevation = new String(Arrays.copyOfRange(ch, start, start + length));
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (localName.equals("trkpt")) {
				Calendar time = parseTime(timeStr);
				GpxTrackPoint routePoint = new GpxTrackPoint(Double.parseDouble(latitude), Double.parseDouble(longitude), time, Integer.parseInt(elevation));
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

		public void warning(SAXParseException spe) throws SAXException {
			System.out.println("Warning: " + getParseExceptionInfo(spe));
		}

		public void error(SAXParseException spe) throws SAXException {
			String message = "Error: " + getParseExceptionInfo(spe);
			throw new SAXException(message);
		}

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

	Calendar parseTime(String dateStr) {
		Date parse;
		try {
			parse = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			throw new ParsingException("Can't parse time", e);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parse);
		return calendar;
	}
}