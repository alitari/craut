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

import org.apache.log4j.Logger;

import de.craut.domain.RoutePoint;

/**
 * Represents an area, defined by its top left and bottom right coordinates
 *
 * @author rgallet
 */
public class BoundingArea {
	Logger logger = Logger.getLogger(getClass());
	private RoutePoint northEast, southWest;
	private RoutePoint southEast, northWest;

	public BoundingArea(RoutePoint northEast, RoutePoint southWest) {
		this.northEast = northEast;
		this.southWest = southWest;

		southEast = new RoutePoint(null, 1, southWest.getLatitude(), northEast.getLongitude());
		northWest = new RoutePoint(null, 2, northEast.getLatitude(), southWest.getLongitude());
	}

	@Deprecated
	public RoutePoint getBottomRight() {
		logger.debug("getBottomRight() is deprecated. Use getSouthWest() instead.");
		return southWest;
	}

	@Deprecated
	public RoutePoint getTopLeft() {
		logger.debug("getTopLeft() is deprecated. Use getNorthEast() instead.");
		return northEast;
	}

	public RoutePoint getNorthEast() {
		return northEast;
	}

	public RoutePoint getSouthWest() {
		return southWest;
	}

	public RoutePoint getSouthEast() {
		return southEast;
	}

	public RoutePoint getNorthWest() {
		return northWest;
	}

	@Override
	public String toString() {
		return "BoundingArea{" + "northEast=" + northEast + ", southWest=" + southWest + '}';
	}

	public boolean isContainedWithin(RoutePoint point) {
		boolean predicate1 = point.getLatitude() >= this.southWest.getLatitude() && point.getLatitude() <= this.northEast.getLatitude();

		if (!predicate1) {
			return false;
		}

		boolean predicate2;

		if (southWest.getLongitude() > northEast.getLongitude()) { // area is
			                                                       // going
			                                                       // across
			// the max/min
			// longitude boundaries
			// (ie. sort of back of
			// the Earth)
			// we "split" the area in 2, longitude-wise, point only needs to be
			// in one or the other.
			boolean predicate3 = point.getLongitude() <= northEast.getLongitude() && point.getLongitude() >= -180;
			boolean predicate4 = point.getLongitude() >= southWest.getLongitude() && point.getLongitude() <= 180;

			predicate2 = predicate3 || predicate4;
		} else {
			predicate2 = point.getLongitude() >= southWest.getLongitude() && point.getLongitude() <= northEast.getLongitude();
		}

		return predicate1 && predicate2;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BoundingArea other = (BoundingArea) obj;
		if (this.northEast != other.northEast && (this.northEast == null || !this.northEast.equals(other.northEast))) {
			return false;
		}
		if (this.southWest != other.southWest && (this.southWest == null || !this.southWest.equals(other.southWest))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 13 * hash + (this.northEast != null ? this.northEast.hashCode() : 0);
		hash = 13 * hash + (this.southWest != null ? this.southWest.hashCode() : 0);
		return hash;
	}
}
