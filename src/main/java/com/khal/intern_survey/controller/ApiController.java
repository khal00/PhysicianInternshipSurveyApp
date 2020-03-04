package com.khal.intern_survey.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khal.intern_survey.dto.CourseEnum;
import com.khal.intern_survey.dto.InternshipSectionsEnum;
import com.khal.intern_survey.dto.MedicalChamberEnum;
import com.khal.intern_survey.entity.InternshipUnit;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.rest.CourseRating;
import com.khal.intern_survey.rest.SectionRating;
import com.khal.intern_survey.rest.UnitRating;
import com.khal.intern_survey.service.InternshipSectionService;
import com.khal.intern_survey.service.InternshipUnitService;
import com.khal.intern_survey.service.QuestionnaireService;

@RestController
@RequestMapping("/api/rating")
public class ApiController {
	
	@Autowired
	InternshipSectionService internshipSectionService;
	
	@Autowired
	QuestionnaireService questionnaireService;
	
	@Autowired
	InternshipUnitService unitService;
	
	@Autowired
	CourseRating courseRating;
	
	@Autowired
	UnitRating unitRating;
	
	@Autowired
	SectionRating sectionRating;
	
	@GetMapping("course/{courseName}/chamber/{medicalChamber}")
	public CourseRating showCourseRatingByChamber(@PathVariable ("courseName") String courseName 
			,@PathVariable ("medicalChamber") String medicalChamber) {
		
		MedicalChamberEnum chamber = MedicalChamberEnum.valueOf(medicalChamber);
		CourseEnum course = CourseEnum.valueOf(courseName);
		
		List<Questionnaire> questionnaires = questionnaireService
				.findAllAcceptedQuestionnairesByMedicalChamber(chamber);
		
		double courseAvg = questionnaireService.calculateCourseAvg(course, questionnaires);
		int numberOfInterns = questionnaireService.getCourseNumberOfInterns(course, questionnaires);
		
		courseRating.setMedicalChamber(chamber);
		courseRating.setName(course);
		courseRating.setRating(courseAvg);
		courseRating.setNumberOfInterns(numberOfInterns);
		
		return courseRating;
	}
	
	@GetMapping("/unit/{unitName}")
	public UnitRating showUnitRating(@PathVariable ("unitName") String unitName) {
		
		InternshipUnit unit = unitService.findByName(unitName);
		List<Questionnaire> questionnaires = questionnaireService.findAllAcceptedQuestionnairesByUnit(unit);
	
		double sectionsAvg = questionnaireService.calculateQuestionnairesAvg(questionnaires);
		
		unitRating.setName(unitName);
		unitRating.setRating(sectionsAvg);
		unitRating.setNumberOfInterns(questionnaires.size());
		
		return unitRating;
	}
	
	@GetMapping("/section/{sectionName}/unit/{unitName}")
	public SectionRating showSectionRatingByUnit(@PathVariable ("sectionName") String sectionName
			, @PathVariable ("unitName") String unitName) {
		
		InternshipSectionsEnum section = InternshipSectionsEnum.valueOf(sectionName);
		InternshipUnit unit = unitService.findByName(unitName);
		List<Questionnaire> questionnaires = questionnaireService.findAllAcceptedQuestionnairesByUnit(unit);
	
		double sectionsAvg = questionnaireService.calculateSectionAvg(section, questionnaires);
		int numberOfInterns = questionnaireService.getSectionNumberOfInterns(section, questionnaires);
		
		sectionRating.setName(sectionName);
		sectionRating.setUnit(unit.getName());
		sectionRating.setRating(sectionsAvg);
		sectionRating.setNumberOfInterns(numberOfInterns);
		
		return sectionRating;
	}
	
	

}
