package de.craut.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "team_event")
public class TeamEvent implements Serializable {

	@Id()
	@Column(name = "id_te", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "name_te", nullable = false)
	private String name;

	@Column(name = "start_te", nullable = true)
	private Date start;

	protected TeamEvent() {
	}

	public TeamEvent(String name, Date start) {
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
