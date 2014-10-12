package de.craut.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SequenceGenerator(name = "pk_sequence", sequenceName = "user_seq", allocationSize = 1)
@Entity
@Table(name = "user")
public class User implements Serializable {

	@Id()
	@Column(name = "us_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "pk_sequence")
	private long id;

	@Column(name = "us_name", nullable = false)
	private String name;

	@Column(name = "us_password", nullable = false)
	private String password;

	protected User() {
	}

	public User(String name, String password) {
		super();
		this.name = name;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
