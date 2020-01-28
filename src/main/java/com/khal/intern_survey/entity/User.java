package com.khal.intern_survey.entity;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String email;

	private String password;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_admin_data", 
		joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id")},
		inverseJoinColumns = { @JoinColumn(name = "admin_data_id", referencedColumnName = "id")})
	private AdminPersonalData adminPersonalData;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "users_roles", 
		joinColumns = @JoinColumn(name = "user_id"), 
		inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles;
	
	private boolean enabled;
	
	@OneToMany(mappedBy = "user")
	private List<Questionnaire> questionnaires;

}
