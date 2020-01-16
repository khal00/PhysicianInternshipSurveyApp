package com.khal.intern_survey.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.khal.intern_survey.DTO.MedicalChamberEnum;
import com.khal.intern_survey.dao.InternshipUnitRepository;
import com.khal.intern_survey.entity.InternshipUnit;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.InternshipUnitService;
import com.khal.intern_survey.service.QuestionnaireService;
import com.khal.intern_survey.service.UserService;

@Controller
@RequestMapping("/survey")
public class SurveyController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	QuestionnaireService questionnaireService;
	
	@Autowired
	InternshipUnitService internshipUnitService;
	
	@GetMapping("/showQuestionnaire")
	public String showQuestionnaire(Principal principal, Model theModel) {
	
		User user = userService.findByEmail(principal.getName());
		Questionnaire questionnaire = user.getQuestionnaire();
		
		// check if user already started the survey if not create new questionnaire
		if (questionnaire == null) {
			questionnaire = new Questionnaire();
			questionnaireService.saveQuestionnaire(questionnaire);
			user.setQuestionnaire(questionnaire);
			userService.saveRegisteredUser(user);
		}
		
		// set list of units. In case user chose medical chamber before filter the units accordingly
		List<InternshipUnit> units;
		
		MedicalChamberEnum medicalChamber = questionnaire.getMedicalChamber();
		
		if (medicalChamber == null) {
			units = internshipUnitService.findAll();
		} else {
			units = internshipUnitService.findByMedicalChamber(medicalChamber.toString());
		}
				
		theModel.addAttribute("questionnaire", questionnaire);
		theModel.addAttribute("units", units);
		
		return "questionnaire_view";
	}
	
	@PostMapping(value = "/saveQuestionnaire", params = "action=save")
	public String saveQuestionnaire(@ModelAttribute ("questionnaire") Questionnaire questionnaire) {
		
		//check if user selected unit but didn't selected chamber name
		if (questionnaire.getMedicalChamber() == null && questionnaire.getUnit() != null) {
			
			InternshipUnit unit = questionnaire.getUnit();
			MedicalChamberEnum chamber = MedicalChamberEnum.valueOf(unit.getMedicalChamber());
			questionnaire.setMedicalChamber(chamber);
		}
		
		questionnaireService.saveQuestionnaire(questionnaire);
		
		return "redirect:/survey/showQuestionnaire";
	}

	@PostMapping(value = "/saveQuestionnaire", params = "action=send")
	public String sendQuestionnaire(@ModelAttribute ("questionnaire") Questionnaire questionnaire) {
		
//		questionnaireService.saveQuestionnaire(questionnaire);
		
		return "redirect:/survey/showQuestionnaire";
	}
	
	// Re-send new units list if user changed medical chamber
	@GetMapping("/unitSearch")
	public String searchUnitByMedicalChamber(Principal principal, Model theModel, @RequestParam (value = "chamberSelected") String chamberSelected) {
		
		List<InternshipUnit> units = internshipUnitService.findByMedicalChamber(chamberSelected);

		User user = userService.findByEmail(principal.getName());
		Questionnaire questionnaire = user.getQuestionnaire();
		
		theModel.addAttribute("questionnaire", questionnaire);
		theModel.addAttribute("units", units);
		
		return "questionnaire_view :: units_list";
	}
	
	
	
}
