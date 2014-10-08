package de.craut.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
@Entity
@Table(name = "activity_point")
public class ActivityPoint implements Serializable {

	@Id()
	@Column(name = "ap_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "ap_ra_id")
	private long activityId;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ap_rp_id", referencedColumnName = "rp_id")
	private RoutePoint routePoint;

	@Column(name = "ap_time", nullable = false)
	private long time;

	@Column(name = "ap_speed", nullable = false)
	private double speed;

	@Column(name = "ap_hr", nullable = false)
	private int heartRate;

	@Column(name = "ap_cad", nullable = false)
	private int cadence;

	@Column(name = "ap_power", nullable = false)
	private int power;

	public ActivityPoint(RoutePoint routePoint, Date time, double speed, int heartRate, int cadence, int power) {
		super();
		this.routePoint = routePoint;
		this.time = time.getTime();
		this.speed = speed;
		this.heartRate = heartRate;
		this.cadence = cadence;
		this.power = power;
	}

	public long getId() {
		return id;
	}

	public int getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(int heartRate) {
		this.heartRate = heartRate;
	}

	public int getCadence() {
		return cadence;
	}

	public void setCadence(int cadence) {
		this.cadence = cadence;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	protected ActivityPoint() {
	}

	public long getActivityId() {
		return activityId;
	}

	public void setActivityId(long activityId) {
		this.activityId = activityId;
	}

	public RoutePoint getRoutePoint() {
		return routePoint;
	}

	public void setRoutePoint(RoutePoint routePoint) {
		this.routePoint = routePoint;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

}
