package de.craut.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "route_point")
public class RoutePoint implements Serializable {

	@Id()
	@Column(name = "rp_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "rp_rt_id", referencedColumnName = "rt_id")
	private Route route;

	@Column(name = "rp_latitude", nullable = false)
	private String latitude;

	@Column(name = "rp_longitude", nullable = false)
	private String longitude;

	protected RoutePoint() {
	}

	public RoutePoint(Route route, String latitude, String longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.route = route;
	}

	public long getId() {
		return id;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
