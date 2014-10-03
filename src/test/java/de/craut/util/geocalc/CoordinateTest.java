/*
 * Copyright (c) 2012 Romain Gallet
 *
 * This file is part of Geocalc.
 *
 * Geocalc is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Geocalc is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Geocalc. If not, see http://www.gnu.org/licenses/.
 */

package de.craut.util.geocalc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.data.geo.Point;

@RunWith(JUnit4.class)
/**
 * @see http://www.csgnetwork.com/gpscoordconv.html for online converter
 */
public class CoordinateTest {

	final Logger logger = Logger.getLogger(getClass());

	@Test
	public void testDegreeCoordinate() {
		DegreeCoordinate degreeCoordinate = new DegreeCoordinate(90);
		Coordinate coordinate = degreeCoordinate.getDegreeCoordinate();
		assertEquals(degreeCoordinate.getDecimalDegrees(), coordinate.getDecimalDegrees(), 1E-4);

		DMSCoordinate dmsCoordinate = new DMSCoordinate(44, 37, 14);
		degreeCoordinate = new DegreeCoordinate(44.620555555555555);
		assertEquals(degreeCoordinate.getDecimalDegrees(), dmsCoordinate.getDecimalDegrees(), 1E-4);
	}

	@Test
	public void testRadianCoordinate() {
		RadianCoordinate radianCoordinate = new RadianCoordinate(Math.PI / 2);
		Coordinate coordinate = radianCoordinate.getDegreeCoordinate();
		assertEquals(radianCoordinate.getDecimalDegrees(), coordinate.getDecimalDegrees(), 1E-4);

		radianCoordinate = new RadianCoordinate(Math.PI * .57);
		DegreeCoordinate degreeCoordinate = new DegreeCoordinate(180 * .57);
		assertEquals(degreeCoordinate.getDecimalDegrees(), radianCoordinate.getDecimalDegrees(), 1E-4);

		DegreeCoordinate convertedBackDegreeCoordinate = radianCoordinate.getDegreeCoordinate();
		assertEquals(degreeCoordinate.getDecimalDegrees(), convertedBackDegreeCoordinate.getDecimalDegrees(), 1E-4);

		DMSCoordinate dmsCoordinate = new DMSCoordinate(44, 37, 14);
		radianCoordinate = new RadianCoordinate(Math.toRadians(44.620555555555555));
		assertEquals(radianCoordinate.getDecimalDegrees(), dmsCoordinate.getDecimalDegrees(), 1E-4);
	}

	@Test
	public void testDMSCoordinate() {
		DMSCoordinate dmsCoordinate = new DMSCoordinate(89, 59, 60);
		Coordinate coordinate = dmsCoordinate.getDegreeCoordinate();
		assertEquals(dmsCoordinate.getDecimalDegrees(), coordinate.getDecimalDegrees(), 1E-4);

		dmsCoordinate = new DMSCoordinate(175, 8, 55);
		DegreeCoordinate degreeCoordinate = new DegreeCoordinate(175.1487);
		assertEquals(degreeCoordinate.getDecimalDegrees(), dmsCoordinate.getDecimalDegrees(), 1E-4);

		DegreeCoordinate convertedBackDegreeCoordinate = dmsCoordinate.getDegreeCoordinate();
		assertEquals(degreeCoordinate.getDecimalDegrees(), convertedBackDegreeCoordinate.getDecimalDegrees(), 1E-4);

		RadianCoordinate radianCoordinate = new RadianCoordinate(Math.PI * 3 / 2);
		dmsCoordinate = new DMSCoordinate(270, 0, 0);
		assertEquals(radianCoordinate.getDMSCoordinate().getDecimalDegrees(), dmsCoordinate.getDecimalDegrees(), 1E-4);
	}

	@Test
	public void testDMSCoordinateNegativeValue() {
		DMSCoordinate dmsCoordinate = new DMSCoordinate(-46, 32, 44.16);
		assertEquals(-46.5456, dmsCoordinate.getDecimalDegrees(), 0);

		DegreeCoordinate degreeCoordinate = new DegreeCoordinate(-46.5456);
		dmsCoordinate = degreeCoordinate.getDMSCoordinate();
		assertEquals(-46, dmsCoordinate.getWholeDegrees(), 0);
		assertEquals(32, dmsCoordinate.getMinutes(), 0);
		assertEquals(44.16, dmsCoordinate.getSeconds(), 0);
	}

	@Test
	public void testGPSCoordinate() {
		GPSCoordinate gpsCoordinate = new GPSCoordinate(89, 60);
		Coordinate coordinate = gpsCoordinate.getDegreeCoordinate();
		assertEquals(gpsCoordinate.getDecimalDegrees(), coordinate.getDecimalDegrees(), 1E-4);

		gpsCoordinate = new GPSCoordinate(175, 8.921999999999457);
		DegreeCoordinate degreeCoordinate = new DegreeCoordinate(175.1487);
		assertEquals(degreeCoordinate.getDecimalDegrees(), gpsCoordinate.getDecimalDegrees(), 1E-4);

		DegreeCoordinate convertedBackDegreeCoordinate = gpsCoordinate.getDegreeCoordinate();
		assertEquals(degreeCoordinate.getDecimalDegrees(), convertedBackDegreeCoordinate.getDecimalDegrees(), 1E-4);

		RadianCoordinate radianCoordinate = new RadianCoordinate(Math.PI * 3 / 2);
		gpsCoordinate = new GPSCoordinate(270, 0);
		assertEquals(radianCoordinate.getDMSCoordinate().getDecimalDegrees(), gpsCoordinate.getDecimalDegrees(), 1E-4);
	}

	@Test
	public void testIsContainedWithin() {
		Point northEast = new Point(70, 145);
		Point southWest = new Point(50, 110);
		BoundingArea boundingArea = new BoundingArea(northEast, southWest);

		Point point1 = new Point(60, 120);
		assertTrue(boundingArea.isContainedWithin(point1));

		Point point2 = new Point(45, 120);
		assertFalse(boundingArea.isContainedWithin(point2));

		Point point3 = new Point(85, 120);
		assertFalse(boundingArea.isContainedWithin(point3));

		Point point4 = new Point(60, 100);
		assertFalse(boundingArea.isContainedWithin(point4));

		Point point5 = new Point(60, 150);
		assertFalse(boundingArea.isContainedWithin(point5));

		Point point6 = new Point(80, 150);
		assertFalse(boundingArea.isContainedWithin(point5));

		Point point7 = new Point(35, 100);
		assertFalse(boundingArea.isContainedWithin(point7));

		northEast = new Point(10, 45);
		southWest = new Point(-30, -35);
		boundingArea = new BoundingArea(northEast, southWest);

		Point point8 = new Point(0, 0);
		assertTrue(boundingArea.isContainedWithin(point8));

		Point point9 = new Point(-5, -30);
		assertTrue(boundingArea.isContainedWithin(point9));

		Point point10 = new Point(5, 30);
		assertTrue(boundingArea.isContainedWithin(point10));

		Point point11 = new Point(-35, 30);
		assertFalse(boundingArea.isContainedWithin(point11));

		northEast = new Point(10, -165);
		southWest = new Point(-30, 170);
		boundingArea = new BoundingArea(northEast, southWest);

		Point point12 = new Point(0, 180);
		assertTrue(boundingArea.isContainedWithin(point12));

		Point point13 = new Point(0, -179);
		assertTrue(boundingArea.isContainedWithin(point13));
	}
}