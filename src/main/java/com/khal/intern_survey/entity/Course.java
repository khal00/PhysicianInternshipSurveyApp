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
	
	@Transient
	private static final int DIVIDERTOTAL = 4;

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
	
	private double rating;
	
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
	
	public double calculateRating() {
		int divider = DIVIDERTOTAL;
		
//		Below code is in case rating fields in questionnaire form are not required
//		if (tutor == 0) divider--;
//		if (unit == 0) divider--;
//		if (theoreticalKnowledge == 0) divider--;
//		if (practicalKnowledge == 0) divider--;
		
		int sum = tutor + unit + theoreticalKnowledge
				+ practicalKnowledge;
		
		if(!disabled) {
			double result = (double) sum / divider;
			this.rating = result;
		}
		
		return 0;
	}

}
