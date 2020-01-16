package com.khal.intern_survey.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.boot.context.properties.ConstructorBinding;

import com.khal.intern_survey.DTO.MedicalChamberEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor(force = true)
@Entity
public class InternshipUnit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private final Long id;
	
	private final String name;
	
	private final String medicalChamber;
	
	@OneToMany(mappedBy = "unit")
	private Set<Questionnaire> questionnaires;

}
