package com.khal.intern_survey.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.khal.intern_survey.entity.InternshipUnit;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.enums.CourseEnum;
import com.khal.intern_survey.enums.MedicalChamberEnum;
import com.khal.intern_survey.rest.CourseRating;
import com.khal.intern_survey.rest.UnitRating;
import com.khal.intern_survey.service.InternshipUnitService;
import com.khal.intern_survey.service.QuestionnaireService;

@Controller
@RequestMapping("/ratings")
public class RatingsController {
	
	@Autowired
	InternshipUnitService internshipUnitService;
	
	@Autowired
	QuestionnaireService questionnaireService;
	
	@GetMapping("/panel")
	public String showRatingsPanel() {
		return "ratings_panel";
	}
	
	@GetMapping("/unitSearch")
	public String showRatingsPanel(@RequestParam ("chamberSelected") MedicalChamberEnum medicalChamber
			, Model theModel) {
		
		List<InternshipUnit> units = internshipUnitService.findByMedicalChamber(medicalChamber);
		
		List<UnitRating> unitsRatings = units
				.stream()
				.filter(u -> questionnaireService.findAllAcceptedQuestionnairesByUnit(u).size() > 0)
				.map(u -> convertUnitToUnitRating(u))
				.sorted()
				.collect(Collectors.toList());
		
		theModel.addAttribute("unitsRatings", checkIfListIsEmptyThenReturnNull(unitsRatings));
		
		List<CourseRating> coursesRatings = getAllCoursesRatings(medicalChamber);
		
		theModel.addAttribute("coursesRatings", coursesRatings);
		
		return "ratings_panel :: ratings_lists";
	}
	
	public <T> List<T> checkIfListIsEmptyThenReturnNull(List<T> list){
		if(list.size() == 0) {
			return null;
		} else return list;
	}
	
	public UnitRating convertUnitToUnitRating(InternshipUnit unit) {
		UnitRating unitRating = new UnitRating(
				unit.getName()
				, questionnaireService.calculateQuestionnairesAvg(unit.getQuestionnaires())
				, unit.getQuestionnaires().size()
				);
		return unitRating;
	}
	
	public List<CourseRating> getAllCoursesRatings(MedicalChamberEnum medicalChamber){
		
		List<Questionnaire> questionnaires = questionnaireService.findAllAcceptedQuestionnairesByMedicalChamber(medicalChamber);
		
		List<CourseRating> courses = new ArrayList<CourseRating>();
		
		for(CourseEnum course : CourseEnum.values()) {
			CourseRating rating = new CourseRating(
					course
					, medicalChamber
					, questionnaireService.calculateCourseAvg(course, questionnaires)
					, questionnaireService.getCourseNumberOfInterns(course, questionnaires)
					);
			if (rating.getRating() > 0) {
				courses.add(rating);
			}
		}
		
		return checkIfListIsEmptyThenReturnNull(courses);
	}

}
