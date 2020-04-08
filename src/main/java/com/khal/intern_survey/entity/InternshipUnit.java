package com.khal.intern_survey.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.khal.intern_survey.enums.MedicalChamberEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
public class InternshipUnit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private MedicalChamberEnum medicalChamber;
	
	@OneToMany(mappedBy = "unit")
	private List<Questionnaire> questionnaires;

}
