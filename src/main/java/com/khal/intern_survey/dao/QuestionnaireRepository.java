package com.khal.intern_survey.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khal.intern_survey.DTO.MedicalChamberEnum;
import com.khal.intern_survey.entity.Questionnaire;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {
	
	public List<Questionnaire> findByStatusAndMedicalChamberOrderBySendDateAsc(Questionnaire.Status status, MedicalChamberEnum medicalChamber);
	
	public List<Questionnaire> findByStatusAndMedicalChamberAndVerificationIdContainingOrderBySendDateAsc
		(Questionnaire.Status status, MedicalChamberEnum medicalChamber, String verificationId);
	
}
