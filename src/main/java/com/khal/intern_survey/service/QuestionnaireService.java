package com.khal.intern_survey.service;

import java.util.List;

import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.entity.User;

public interface QuestionnaireService {
	
	public Questionnaire saveQuestionnaire(Questionnaire questionnaire);

	public Questionnaire findById(Long id);

	public void delete(Long id);

	public List<Questionnaire> findByStatusAndMedicalChamber(User adminUser);

	public List<Questionnaire> searchByVerificationId(User adminUser, String id);

}
