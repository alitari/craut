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
import javax.persistence.OneToOne;
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
	private Activity activity;

	@OneToOne(optional = false)
	@JoinColumn(name = "ap_rp_id", referencedColumnName = "rp_id")
	private RoutePoint routePoint;

	@Column(name = "ap_time", nullable = false)
	private Date time;

	protected ActivityPoint() {
	}

	public ActivityPoint(Activity activity, RoutePoint routePoint, Date time) {
		super();
		this.routePoint = routePoint;
		this.activity = activity;
		this.time = time;
	}

	public long getId() {
		return id;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public RoutePoint getRoutePoint() {
		return routePoint;
	}

	public void setRoutePoint(RoutePoint routePoint) {
		this.routePoint = routePoint;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
