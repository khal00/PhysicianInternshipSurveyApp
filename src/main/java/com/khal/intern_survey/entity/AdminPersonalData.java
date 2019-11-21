package com.khal.intern_survey.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "admin_personal_data")
public class AdminPersonalData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@NotBlank(message = "is required")
	@Column(name = "first_name")
	private String firstName;
	
	@NotBlank(message = "is required")
	@Column(name = "last_name")
	private String lastName;
	
	@NotBlank(message = "is required")
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@NotBlank(message = "is required")
	@Column(name = "medical_chamber")
	private String medicalChamber;
	
	@OneToOne(mappedBy = "adminPersonalData")
	private User user;


	public AdminPersonalData() {
	}

	public AdminPersonalData(String firstName, String lastName, String phoneNumber, String medicalChamber, User user) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.medicalChamber = medicalChamber;
		this.user = user;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMedicalChamber() {
		return medicalChamber;
	}

	public void setMedicalChamber(String medicalChamber) {
		this.medicalChamber = medicalChamber;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
	

}
