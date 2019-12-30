package com.khal.intern_survey.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khal.intern_survey.dao.QuestionnaireRepository;
import com.khal.intern_survey.entity.Questionnaire;

@Service
public class QestionnaireServiceImpl implements QuestionnaireService {
	
	@Autowired
	QuestionnaireRepository questionnaireRepository;

	@Override
	public void saveQuestionnaire(Questionnaire questionnaire) {

		questionnaireRepository.save(questionnaire);
		
	}
	
}
