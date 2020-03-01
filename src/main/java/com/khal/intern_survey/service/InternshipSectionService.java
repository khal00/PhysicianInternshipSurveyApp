package com.khal.intern_survey.service;

import com.khal.intern_survey.entity.InternshipSection;
import com.khal.intern_survey.entity.Questionnaire;

public interface InternshipSectionService {
	
	public void saveSection(InternshipSection section);
	
	public void createQuestionnaireSections(Questionnaire questionnaire);
	
	double calculateSectionAvg(String sectionName, Long questionnaireId);

}
