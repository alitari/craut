package de.craut.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SequenceGenerator(name = "pk_sequence", sequenceName = "route_seq", allocationSize = 1)
@Entity
@Table(name = "route")
public class Route implements Serializable {

	@Id()
	@Column(name = "rt_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "pk_sequence")
	private long id;

	@Column(name = "rt_name", nullable = false)
	private String name;

	@Column(name = "rt_start_latitude", nullable = false)
	private double startLatitude;

	@Column(name = "rt_start_longitude", nullable = false)
	private double startLongitude;

	@Column(name = "rt_end_latitude", nullable = false)
	private double endLatitude;

	@Column(name = "rt_end_longitude", nullable = false)
	private double endLongitude;

	@Column(name = "rt_distance", nullable = false)
	private double distance;

	@Column(name = "rt_elevation", nullable = true)
	private int elevation;

	protected Route() {
	}

	public Route(String name) {
		super();
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getStartLatitude() {
		return startLatitude;
	}

	public void setStartLatitude(double startLatitude) {
		this.startLatitude = startLatitude;
	}

	public double getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(double startLongitude) {
		this.startLongitude = startLongitude;
	}

	public double getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(double endLatitude) {
		this.endLatitude = endLatitude;
	}

	public double getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(double endLongitude) {
		this.endLongitude = endLongitude;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int getElevation() {
		return elevation;
	}

	public void setElevation(int elevation) {
		this.elevation = elevation;
	}

	@JsonIgnore
	public double getAscent() {
		return (elevation / distance) * 100;
	}

	@JsonIgnore
	public double getDifficulty() {
		double ascent = getAscent();
		return 0.1 * ascent * ascent * (distance / 1000);
	}

	@JsonIgnore
	public int getDifficultyStars() {
		double difficulty = getDifficulty();
		if (difficulty < 10)
			return 0;
		if (difficulty < 20)
			return 1;
		if (difficulty < 40)
			return 2;
		if (difficulty < 80)
			return 3;
		if (difficulty < 120)
			return 4;
		return 5;
	}

	@Override
	public String toString() {
		return "Route [id=" + id + ", name=" + name + "]";
	}

}
