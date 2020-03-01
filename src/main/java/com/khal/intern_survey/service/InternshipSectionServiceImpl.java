package com.khal.intern_survey.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khal.intern_survey.DTO.InternshipSectionsEnum;
import com.khal.intern_survey.dao.InternshipSectionRepository;
import com.khal.intern_survey.entity.InternshipSection;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.entity.InternshipSection;

@Service
public class InternshipSectionServiceImpl implements InternshipSectionService {
	
	@Autowired
	InternshipSectionRepository sectionRepository;

	@Override
	public void saveSection(InternshipSection section) {
		
		sectionRepository.save(section);

	}
	
	@Override
	public void createQuestionnaireSections(Questionnaire questionnaire) {
		
		for(InternshipSectionsEnum sectionName : InternshipSectionsEnum.values()) {
			InternshipSection section = new InternshipSection(sectionName, questionnaire);
			sectionRepository.save(section);
		}
		
	}

	@Override
	public double calculateSectionAvg(String sectionName, Long questionnaireId) {
		
		return sectionRepository.calculateSectionAvg(sectionName, questionnaireId);
	}

}
