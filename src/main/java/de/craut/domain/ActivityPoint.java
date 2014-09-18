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

@Entity
@Table(name = "activity_point")
public class ActivityPoint implements Serializable {

	@Id()
	@Column(name = "ap_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ap_ra_id", referencedColumnName = "ra_id")
	private RouteActivity routeActivity;

	@Column(name = "ap_time", nullable = false)
	private Date time;

	protected ActivityPoint() {
	}

	public ActivityPoint(RouteActivity routeActivity, Date time) {
		super();
		this.routeActivity = routeActivity;
		this.time = time;
	}

	public long getId() {
		return id;
	}

}
