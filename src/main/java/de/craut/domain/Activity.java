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

	protected Activity() {
	}

	public Activity(String name, Route route, Date start) {
		super();
		this.name = name;
		this.route = route;
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

	@JsonIgnore
	public String getStartDay() {
		return AdvancedDateFormat.day(start);
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

}
