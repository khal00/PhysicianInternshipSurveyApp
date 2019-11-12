package com.krzhal.PhysicianInternshipSurveyApp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "authorities")
public class Role {
	
	@Column(name = "username")
	@Id
	private String username;
	
	@Column(name = "authority")
	private String authority;
	
	@OneToOne(mappedBy = "role")
	private User user;

	public Role() {
	}

	public Role(String username, String authority) {
		this.username = username;
		this.authority = authority;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	@Override
	public String toString() {
		return "Role [username=" + username + ", authority=" + authority + ", user=" + user + "]";
	}
	

}
