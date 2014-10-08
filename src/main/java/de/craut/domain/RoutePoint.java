package de.craut.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.data.geo.Point;

@SequenceGenerator(name = "pk_sequence", sequenceName = "route_point_seq", allocationSize = 1)
@Entity
@Table(name = "route_point")
public class RoutePoint extends Point {

	@Id()
	@Column(name = "rp_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "pk_sequence")
	private long id;

	@Column(name = "rp_rt_id")
	private long routeId;

	@Column(name = "rp_seq", nullable = false)
	private int sequence;

	@Column(name = "rp_latitude", nullable = false)
	private double latitude;

	@Column(name = "rp_longitude", nullable = false)
	private double longitude;

	@Column(name = "rp_elevation", nullable = false)
	private int elevation;

	public RoutePoint(int sequence, String latitude, String longitude, int elevation) {
		this(sequence, Double.parseDouble(latitude), Double.parseDouble(longitude), elevation);
	}

	protected RoutePoint() {
		super(0, 0);
	}

	public RoutePoint(double latitude, double longitude) {
		this(0, latitude, longitude, 0);
	}

	public RoutePoint(int sequence, double latitude, double longitude, int elevation) {
		super(latitude, longitude);
		this.sequence = sequence;
		this.latitude = latitude;
		this.longitude = longitude;
		this.elevation = elevation;
	}

	public long getId() {
		return id;
	}

	public double getLatitude() {
		return latitude;
	}

	@Override
	public double getX() {
		return getLatitude();
	}

	@Override
	public double getY() {
		return getLongitude();
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public long getRouteId() {
		return routeId;
	}

	public void setRoute(long route) {
		this.routeId = route;
	}

	public int getElevation() {
		return elevation;
	}

	public void setElevation(int elevation) {
		this.elevation = elevation;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final RoutePoint other = (RoutePoint) obj;
		if (Double.doubleToLongBits(this.latitude) != Double.doubleToLongBits(other.latitude)) {
			return false;
		}
		if (Double.doubleToLongBits(this.longitude) != Double.doubleToLongBits(other.longitude)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 31 * hash + (int) (Double.doubleToLongBits(this.latitude) ^ (Double.doubleToLongBits(this.latitude) >>> 32));
		hash = 31 * hash + (int) (Double.doubleToLongBits(this.longitude) ^ (Double.doubleToLongBits(this.longitude) >>> 32));
		return hash;
	}

	@Override
	public String toString() {
		return "Point{" + "latitude=" + latitude + ", longitude=" + longitude + '}';
	}

}
