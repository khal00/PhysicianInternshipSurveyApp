package com.khal.intern_survey.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.khal.intern_survey.DTO.MedicalChamberEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "questionnaires")
public class Questionnaire {
	
	private enum Status{
		DRAFT, SENT, ACCEPTED;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Status status = Status.DRAFT;
	
	private MedicalChamberEnum medicalChamber;
	
	@ManyToOne
	@JoinColumn(name="unit_id")
	private InternshipUnit unit;
	
	@OneToOne(mappedBy = "questionnaire")
	private User user;

}
