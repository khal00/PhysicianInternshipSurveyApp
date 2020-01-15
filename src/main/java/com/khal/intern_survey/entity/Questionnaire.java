package com.khal.intern_survey.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.khal.intern_survey.DTO.MedicalChamberEnum;

@Entity
@Table(name = "questionnaires")
public class Questionnaire {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String status;
	
	private MedicalChamberEnum medicalChamber;
	
	private String unitName;
	
	@OneToOne(mappedBy = "questionnaire")
	private User user;
	
	private static final String DRAFT = "draft";

	public Questionnaire() {
		super();
		this.status = DRAFT;
	}

	public Questionnaire(MedicalChamberEnum medicalChamber, String unitName) {
		super();
		this.status = DRAFT;
		this.medicalChamber = medicalChamber;
		this.unitName = unitName;
	}



	public Questionnaire(MedicalChamberEnum medicalChamber, String unitName, User user) {
		super();
		this.status = DRAFT;
		this.medicalChamber = medicalChamber;
		this.unitName = unitName;
		this.user = user;
	}

	public Questionnaire(String status, MedicalChamberEnum medicalChamber, String unitName, User user) {
		super();
		this.status = status;
		this.medicalChamber = medicalChamber;
		this.unitName = unitName;
		this.user = user;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MedicalChamberEnum getMedicalChamber() {
		return medicalChamber;
	}

	public void setMedicalChamber(MedicalChamberEnum medicalChamber) {
		this.medicalChamber = medicalChamber;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	

}
