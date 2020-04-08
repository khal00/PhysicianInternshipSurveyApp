package com.khal.intern_survey.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khal.intern_survey.entity.InternshipUnit;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.enums.MedicalChamberEnum;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {
	
	public List<Questionnaire> findByStatusAndMedicalChamberOrderByIdAsc(Questionnaire.Status status, MedicalChamberEnum medicalChamber);
	
	public List<Questionnaire> findByStatusAndMedicalChamberAndVerificationIdContainingOrderByIdAsc
		(Questionnaire.Status status, MedicalChamberEnum medicalChamber, String verificationId);
	
	public List<Questionnaire> findByStatusAndUnit(Questionnaire.Status status, InternshipUnit unit);
	
}
