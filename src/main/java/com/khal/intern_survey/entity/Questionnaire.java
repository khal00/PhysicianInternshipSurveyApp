package com.khal.intern_survey.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "questionnaires")
public class Questionnaire {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String status;
	
	private String medicalChamber;
	
	private String unitName;
	
	@OneToOne(mappedBy = "questionnaire")
	private User user;
	
	private static final String DRAFT = "draft";

	public Questionnaire() {
		super();
	}

	public Questionnaire(String medicalChamber, String unitName) {
		super();
		this.status = DRAFT;
		this.medicalChamber = medicalChamber;
		this.unitName = unitName;
	}



	public Questionnaire(String medicalChamber, String unitName, User user) {
		super();
		this.status = DRAFT;
		this.medicalChamber = medicalChamber;
		this.unitName = unitName;
		this.user = user;
	}

	public Questionnaire(String status, String medicalChamber, String unitName, User user) {
		super();
		this.status = status;
		this.medicalChamber = medicalChamber;
		this.unitName = unitName;
		this.user = user;
	}



	protected Long getId() {
		return id;
	}

	protected void setId(Long id) {
		this.id = id;
	}

	protected String getStatus() {
		return status;
	}

	protected void setStatus(String status) {
		this.status = status;
	}

	protected String getMedicalChamber() {
		return medicalChamber;
	}

	protected void setMedicalChamber(String medicalChamber) {
		this.medicalChamber = medicalChamber;
	}

	protected String getUnitName() {
		return unitName;
	}

	protected void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	protected User getUser() {
		return user;
	}

	protected void setUser(User user) {
		this.user = user;
	}
	

}
