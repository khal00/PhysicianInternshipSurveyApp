package com.krzhal.PhysicianInternshipSurveyApp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name = "user_admin_data", 
		joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id")},
		inverseJoinColumns = { @JoinColumn(name = "admin_data_id", referencedColumnName = "id")})
	private AdminPersonalData adminPersonalData;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "username", referencedColumnName = "username")
	private Role role;

	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AdminPersonalData getAdminPersonalData() {
		return adminPersonalData;
	}

	public void setAdminPersonalData(AdminPersonalData adminPersonalData) {
		this.adminPersonalData = adminPersonalData;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", adminPersonalData="
				+ adminPersonalData + ", role=" + role + "]";
	}

}
