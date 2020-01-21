package com.khal.intern_survey.service;

import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.entity.User;

public interface QuestionnaireService {
	
	public void saveQuestionnaire(Questionnaire questionnaire);

	public Questionnaire findById(Long id);

	public void delete(Long id);

}
