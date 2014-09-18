package de.craut.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "route_activity")
public class RouteActivity implements Serializable {

	@Id()
	@Column(name = "ra_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToOne(optional = false)
	@JoinColumn(name = "ra_rt_id", referencedColumnName = "rt_id")
	private Route route;

	@Column(name = "ra_name", nullable = false)
	private String name;

	@Column(name = "ra_start", nullable = true)
	private Date start;

	protected RouteActivity() {
	}

	public RouteActivity(String name, Date start) {
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

	@JsonIgnore
	public String getStartFormatted() {
		return new SimpleDateFormat("hh:mm dd.MM.yyyy").format(start);
	}

}
