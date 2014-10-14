package de.craut.domain;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.craut.util.AdvancedDateFormat;

@SequenceGenerator(name = "pk_sequence", sequenceName = "activity_seq", allocationSize = 1)
@Entity
@Table(name = "activity")
public class Activity implements Serializable {

	@Id()
	@Column(name = "ra_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "pk_sequence")
	private long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ra_us_id", referencedColumnName = "us_id")
	private User user;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ra_rt_id", referencedColumnName = "rt_id")
	private Route route;

	@Column(name = "ra_name", nullable = false)
	private String name;

	@Column(name = "ra_start", nullable = true)
	private long start;

	@Column(name = "ra_end", nullable = true)
	private long end;

	@Column(name = "ra_pow_avg", nullable = true)
	private double powerAverage;

	@Column(name = "ra_pow_max", nullable = true)
	private int powerMax;

	@Column(name = "ra_pow_min", nullable = true)
	private int powerMin;

	@Column(name = "ra_cad_avg", nullable = true)
	private double cadenceAverage;

	@Column(name = "ra_cad_max", nullable = true)
	private int cadenceMax;

	@Column(name = "ra_cad_min", nullable = true)
	private int cadenceMin;

	@Column(name = "ra_speed_avg", nullable = true)
	private double speedAverage;

	@Column(name = "ra_speed_max", nullable = true)
	private double speedMax;

	@Column(name = "ra_speed_min", nullable = true)
	private double speedMin;

	@Column(name = "ra_hr_avg", nullable = true)
	private double heartRateAverage;

	@Column(name = "ra_hr_max", nullable = true)
	private int heartRateMax;

	@Column(name = "ra_hr_min", nullable = true)
	private int heartRateMin;

	protected Activity() {
	}

	public Activity(String name, User user, Route route, long start, long end) {
		super();
		this.name = name;
		this.user = user;
		this.route = route;
		this.start = start;
		this.end = end;
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

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	@JsonIgnore
	public String getStartDay() {
		return AdvancedDateFormat.day(start);
	}

	@JsonIgnore
	public long getTime() {
		return end - start;
	}

	@JsonIgnore
	public String getTimeFormatted() {
		long millis = getTime();
		return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
		        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
		        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
	}

	@JsonIgnore
	public double getSpeed() {
		long time = (int) getTime();
		double distance = getRoute().getDistance();
		double hour = (double) time / (1000 * 60 * 60);
		double km = distance / 1000;
		return km / hour;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public double getPowerAverage() {
		return powerAverage;
	}

	public void setPowerAverage(double powerAverage) {
		this.powerAverage = powerAverage;
	}

	public int getPowerMax() {
		return powerMax;
	}

	public void setPowerMax(int powerMax) {
		this.powerMax = powerMax;
	}

	public int getPowerMin() {
		return powerMin;
	}

	public void setPowerMin(int powerMin) {
		this.powerMin = powerMin;
	}

	public double getCadenceAverage() {
		return cadenceAverage;
	}

	public void setCadenceAverage(double cadenceAverage) {
		this.cadenceAverage = cadenceAverage;
	}

	public int getCadenceMax() {
		return cadenceMax;
	}

	public void setCadenceMax(int cadenceMax) {
		this.cadenceMax = cadenceMax;
	}

	public int getCadenceMin() {
		return cadenceMin;
	}

	public void setCadenceMin(int cadenceMin) {
		this.cadenceMin = cadenceMin;
	}

	public double getSpeedAverage() {
		return speedAverage;
	}

	public void setSpeedAverage(double speedAverage) {
		this.speedAverage = speedAverage;
	}

	public double getSpeedMax() {
		return speedMax;
	}

	public void setSpeedMax(double speedMax) {
		this.speedMax = speedMax;
	}

	public double getSpeedMin() {
		return speedMin;
	}

	public void setSpeedMin(double speedMin) {
		this.speedMin = speedMin;
	}

	public double getHeartRateAverage() {
		return heartRateAverage;
	}

	public void setHeartRateAverage(double heartRateAverage) {
		this.heartRateAverage = heartRateAverage;
	}

	public int getHeartRateMax() {
		return heartRateMax;
	}

	public void setHeartRateMax(int heartRateMax) {
		this.heartRateMax = heartRateMax;
	}

	public int getHeartRateMin() {
		return heartRateMin;
	}

	public void setHeartRateMin(int heartRateMin) {
		this.heartRateMin = heartRateMin;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
