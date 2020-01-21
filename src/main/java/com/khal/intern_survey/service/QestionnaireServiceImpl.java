package com.khal.intern_survey.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khal.intern_survey.dao.QuestionnaireRepository;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.entity.User;

@Service
public class QestionnaireServiceImpl implements QuestionnaireService {
	
	@Autowired
	QuestionnaireRepository questionnaireRepository;

	@Override
	public void saveQuestionnaire(Questionnaire questionnaire) {

		questionnaireRepository.save(questionnaire);
		
	}

	@Override
	public Questionnaire findById(Long id) {
		
		Optional<Questionnaire> questionnaire = questionnaireRepository.findById(id);
		return questionnaire.get();
	}

	@Override
	public void delete(Long id) {
		questionnaireRepository.deleteById(id);
		
	}


	
}
