package com.khal.intern_survey.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.khal.intern_survey.DTO.InternshipSectionsEnum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
public class InternshipSection {
	
	@Transient
	private static final int DIVIDERTOTAL = 9;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private InternshipSectionsEnum name;
	
	private String tutorName;
	
	private String unitName;

	private int tutor;
	
	private int unit;
	
	private int numberOfProcedures;
	
	private int proceduresAutonomy;
	
	private int theoreticalKnowledge;
	
	private int practicalKnowledge;
	
	private int medicalDuty;
	
	private int ward;
	
	private int clinic;
	
	private double rating;
	
	private boolean disabled;
	
	@ManyToOne
	@JoinColumn(name = "questionnaire_id")
	private Questionnaire questionnaire;
	
	public InternshipSection(InternshipSectionsEnum name, Questionnaire questionnaire) {
		super();
		this.name = name;
		this.questionnaire = questionnaire;
	}
	
	public InternshipSection(InternshipSectionsEnum name, String tutorName, String unitName, int tutor, int unit,
			int numberOfProcedures, int proceduresAutonomy, int theoreticalKnowledge, int practicalKnowledge,
			int medicalDuty, int ward, int clinic, Questionnaire questionnaire) {
		super();
		this.name = name;
		this.tutorName = tutorName;
		this.unitName = unitName;
		this.tutor = tutor;
		this.unit = unit;
		this.numberOfProcedures = numberOfProcedures;
		this.proceduresAutonomy = proceduresAutonomy;
		this.theoreticalKnowledge = theoreticalKnowledge;
		this.practicalKnowledge = practicalKnowledge;
		this.medicalDuty = medicalDuty;
		this.ward = ward;
		this.clinic = clinic;
		this.questionnaire = questionnaire;
	}
	
	
	
	public double calculateRating() {
		int divider = DIVIDERTOTAL;

//		Below code is in case rating fields in questionnaire form are not required
//		if (tutor == 0) divider--;
//		if (unit == 0) divider--;
//		if (numberOfProcedures == 0) divider--;
//		if (proceduresAutonomy == 0) divider--;
//		if (theoreticalKnowledge == 0) divider--;
//		if (practicalKnowledge == 0) divider--;
//		if (medicalDuty == 0) divider--;
//		if (ward == 0) divider--;
//		if (clinic == 0) divider--;
		
		int sum = tutor + unit + numberOfProcedures + proceduresAutonomy + theoreticalKnowledge
				+ practicalKnowledge + medicalDuty + ward + clinic;
		
		if(!disabled) {
			double result = (double) sum / divider;
			this.rating = result;
		}
		
		return 0;
	}

}
