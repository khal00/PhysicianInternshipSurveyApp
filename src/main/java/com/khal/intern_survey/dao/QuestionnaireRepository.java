package com.khal.intern_survey.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khal.intern_survey.entity.Questionnaire;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {
	
}
