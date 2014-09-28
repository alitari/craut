package de.craut.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.craut.util.AdvancedDateFormat;

@Entity
@Table(name = "route")
public class Route implements Serializable {

	@Id()
	@Column(name = "rt_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "rt_name", nullable = false)
	private String name;

	@Column(name = "rt_start", nullable = true)
	private Date start;

	@Column(name = "rt_start_latitude", nullable = false)
	private double startLatitude;

	@Column(name = "rt_start_longitude", nullable = false)
	private double startLongitude;

	@Column(name = "rt_end_latitude", nullable = false)
	private double endLatitude;

	@Column(name = "rt_end_longitude", nullable = false)
	private double endLongitude;

	protected Route() {
	}

	public Route(String name, Date start) {
		super();
		this.name = name;
		this.start = start;
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

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
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

	@JsonIgnore
	public String getStartDay() {
		return AdvancedDateFormat.day(start);
	}

	@Override
	public String toString() {
		return "Route [id=" + id + ", name=" + name + "]";
	}

}
