package com.khal.intern_survey.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.khal.intern_survey.DTO.MedicalChamberEnum;
import com.khal.intern_survey.validation.ContactNumberConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
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
	
	@NotBlank(message = "index.phoneisrequired")
	@ContactNumberConstraint
	@Column(name = "phone_number")
	private String phoneNumber;
	
	@NotNull(message = "is required")
	@Column(name = "medical_chamber")
	private MedicalChamberEnum medicalChamber;
	
	@OneToOne(mappedBy = "adminPersonalData")
	private User user;

}
