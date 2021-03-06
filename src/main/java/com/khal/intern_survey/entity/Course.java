package com.khal.intern_survey.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.khal.intern_survey.dto.CourseEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private CourseEnum name;
	
	private String tutorName;
	
	private String unitName;
	
	private int tutor;
	
	private int unit;
	
	private int theoreticalKnowledge;
	
	private int practicalKnowledge;
	
	private boolean disabled;
	
	@ManyToOne
	@JoinColumn(name = "questionnaire_id")
	private Questionnaire questionnaire;
	
	public Course(CourseEnum name, String tutorName, String unitName, int tutor, int unit, int theoreticalKnowledge,
			int practicalKnowledge, Questionnaire questionnaire) {
		super();
		this.name = name;
		this.tutorName = tutorName;
		this.unitName = unitName;
		this.tutor = tutor;
		this.unit = unit;
		this.theoreticalKnowledge = theoreticalKnowledge;
		this.practicalKnowledge = practicalKnowledge;
		this.questionnaire = questionnaire;
	}

	public Course(CourseEnum courseName, Questionnaire questionnaire) {
		super();
		this.name = courseName;
		this.questionnaire = questionnaire;
	}

}
