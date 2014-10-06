package de.craut.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.craut.util.AdvancedDateFormat;

@Entity
@Table(name = "activity")
public class Activity implements Serializable {

	@Id()
	@Column(name = "ra_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ra_rt_id", referencedColumnName = "rt_id")
	private Route route;

	@Column(name = "ra_name", nullable = false)
	private String name;

	@Column(name = "ra_start", nullable = true)
	private Date start;

	@Column(name = "ra_end", nullable = true)
	private Date end;

	protected Activity() {
	}

	public Activity(String name, Route route, Date start, Date end) {
		super();
		this.name = name;
		this.route = route;
		this.start = start;
		this.setEnd(end);
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

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@JsonIgnore
	public String getStartDay() {
		return AdvancedDateFormat.day(start);
	}

	@JsonIgnore
	public long getTime() {
		return end.getTime() - start.getTime();
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

}
