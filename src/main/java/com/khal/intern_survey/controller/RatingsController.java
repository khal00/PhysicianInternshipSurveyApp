package com.khal.intern_survey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khal.intern_survey.DTO.MedicalChamberEnum;
import com.khal.intern_survey.entity.InternshipUnit;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.rest.UnitRating;
import com.khal.intern_survey.service.InternshipSectionService;
import com.khal.intern_survey.service.InternshipUnitService;
import com.khal.intern_survey.service.QuestionnaireService;

@RestController
@RequestMapping("/api/rating")
public class RatingsController {
	
	@Autowired
	InternshipSectionService internshipSectionService;
	
	@Autowired
	QuestionnaireService questionnaireService;
	
	@Autowired
	InternshipUnitService unitService;
	
	@GetMapping("/by_chamber/{medicalChamber}")
	public UnitRating showQuestionnairesRatingByChamber(@PathVariable ("medicalChamber") String medicalChamber) {
		
		MedicalChamberEnum chamber = MedicalChamberEnum.valueOf(medicalChamber);
		
		List<Questionnaire> questionnaires = questionnaireService
				.findAllAcceptedQuestionnairesByMedicalChamber(chamber);
	
		double sectionsAvg = questionnaireService.calculateMultipleQuestionnairesSectionsAvg(questionnaires);
		double coursesAvg = questionnaireService.calculateMultipleQuestionnairesCoursesAvg(questionnaires);
		
		System.out.println(questionnaires);
		
		UnitRating rating = new UnitRating(medicalChamber, sectionsAvg, questionnaires.size());
		
		return rating;
	}
	
	@GetMapping("/by_unit/{unitName}")
	public UnitRating showQuestionnairesRatingByUnit(@PathVariable ("unitName") String unitName) {
		
		InternshipUnit unit = unitService.findByName(unitName);
		List<Questionnaire> questionnaires = questionnaireService.findAllAcceptedQuestionnairesByUnit(unit);
	
		double sectionsAvg = questionnaireService.calculateMultipleQuestionnairesSectionsAvg(questionnaires);
		double coursesAvg = questionnaireService.calculateMultipleQuestionnairesCoursesAvg(questionnaires);
		
		UnitRating rating = new UnitRating(unitName, sectionsAvg, questionnaires.size());
		
		return rating;
	}
	
	

}
