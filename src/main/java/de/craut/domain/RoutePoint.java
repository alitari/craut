package de.craut.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.geo.Point;

@Entity
@Table(name = "route_point")
public class RoutePoint extends Point {

	@Id()
	@Column(name = "rp_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "rp_rt_id", referencedColumnName = "rt_id")
	private Route route;

	@Column(name = "rp_seq", nullable = false)
	private int sequence;

	@Column(name = "rp_latitude", nullable = false)
	private double latitude;

	@Column(name = "rp_longitude", nullable = false)
	private double longitude;

	public RoutePoint(Route route, int sequence, String latitude, String longitude) {
		this(route, sequence, Double.parseDouble(latitude), Double.parseDouble(longitude));
	}

	protected RoutePoint() {
		super(0, 0);
	}

	public RoutePoint(Route route, int sequence, double latitude, double longitude) {
		super(latitude, longitude);
		this.sequence = sequence;
		this.latitude = latitude;
		this.longitude = longitude;
		this.setRoute(route);
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

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

}
