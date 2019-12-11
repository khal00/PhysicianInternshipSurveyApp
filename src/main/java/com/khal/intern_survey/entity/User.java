package com.khal.intern_survey.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_admin_data", 
		joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id")},
		inverseJoinColumns = { @JoinColumn(name = "admin_data_id", referencedColumnName = "id")})
	private AdminPersonalData adminPersonalData;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", 
		joinColumns = @JoinColumn(name = "user_id"), 
		inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles;
	
	@Column(name = "enabled")
	private boolean enabled;

	public User() {
	}

	public User(String email, String username, String password, Collection<Role> roles) {
		super();
		this.email = email;
		this.password = password;
		this.roles = roles;
		this.enabled = false;
	}

	public User(String email, String username, String password, AdminPersonalData adminPersonalData,
			Collection<Role> roles) {
		super();
		this.email = email;
		this.password = password;
		this.adminPersonalData = adminPersonalData;
		this.roles = roles;
		this.enabled = false;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", adminPersonalData="
				+ adminPersonalData + ", enabled=" + enabled + "]";
	}

}
