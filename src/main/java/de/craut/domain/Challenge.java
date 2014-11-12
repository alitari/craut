package de.craut.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SequenceGenerator(name = "pk_sequence", sequenceName = "challenge_seq", allocationSize = 1)
@Entity
@Table(name = "challenge")
public class Challenge implements Serializable {

	@Id()
	@Column(name = "ch_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "pk_sequence")
	private long id;

	@Column(name = "ch_name", nullable = false)
	private String name;

	@JoinTable(name = "challenge_has_routes")
	@ManyToMany
	private List<Route> routes;

	protected Challenge() {
	}

	public Challenge(String name) {
		super();
		this.name = name;
		routes = new ArrayList<Route>();
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

	public List<Route> getRoutes() {
		return routes;
	}

	@Override
	public String toString() {
		return "Route [id=" + id + ", name=" + name + "]";
	}

}
