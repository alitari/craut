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

	@JsonIgnore
	public String getStartDay() {
		return AdvancedDateFormat.day(start);
	}

	@Override
	public String toString() {
		return "Route [id=" + id + ", name=" + name + "]";
	}

}
