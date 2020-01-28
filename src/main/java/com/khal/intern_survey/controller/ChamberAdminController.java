package com.khal.intern_survey.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.khal.intern_survey.DTO.MedicalChamberEnum;
import com.khal.intern_survey.entity.InternshipUnit;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.entity.Questionnaire.Status;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.InternshipUnitService;
import com.khal.intern_survey.service.QuestionnaireService;
import com.khal.intern_survey.service.UserService;

@Controller
@RequestMapping("/chamber")
public class ChamberAdminController {
	
	@Autowired
	QuestionnaireService questionnaireService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	InternshipUnitService internshipUnitService;
	
	@GetMapping("/showQuestListForAcceptance")
	public String showQuestionnairesListForAcceptance(Principal principal, Model theModel) {
		
		User adminUser = userService.findByEmail(principal.getName());
		
		List<Questionnaire> questionnaires = questionnaireService.findByStatusAndMedicalChamber(adminUser);
		
		theModel.addAttribute("questionnaires", questionnaires);
		
		return "admin_questionnaires_list";
	}
	
	@GetMapping("/searchQuestListByVerificationId")
	public String searchQuestListByVerificationId(Principal principal, Model theModel, @RequestParam (value = "verId") String verId) {
		
		User adminUser = userService.findByEmail(principal.getName());
		
		List<Questionnaire> questionnaires = questionnaireService.searchByVerificationId(adminUser, verId);
		
		theModel.addAttribute("questionnaires", questionnaires);
		
		return "admin_questionnaires_list";
	}
	
	@GetMapping("/showQuestionnaire/{id}")
	public String showQuestionnaire(Model theModel, @PathVariable ("id") long id) {
	
		Questionnaire questionnaire = questionnaireService.findById(id);
		
		List<InternshipUnit> units;
		
		MedicalChamberEnum medicalChamber = questionnaire.getMedicalChamber();
		
		units = internshipUnitService.findByMedicalChamber(medicalChamber.toString());
		
		if (!theModel.containsAttribute("questionnaire")) {
			theModel.addAttribute("questionnaire", questionnaire);
		}
		theModel.addAttribute("units", units);
		
		return "questionnaire_view";
	}
	
	@GetMapping("/acceptQuest/{id}")
	public String acceptQuestionnaire(@PathVariable ("id") long id) {
	
		Questionnaire questionnaire = questionnaireService.findById(id);
		
		questionnaire.setStatus(Status.ACCEPTED);
		questionnaireService.saveQuestionnaire(questionnaire);
		
		return "redirect:/chamber/showQuestListForAcceptance";
	}
	
	@GetMapping("/deleteQuest/{id}")
	public String deleteQuestionnaire(Principal principal, @PathVariable ("id") long id) {
		
		Questionnaire questionnaire = questionnaireService.findById(id);
		User user = userService.findByEmail(principal.getName());
		
//		check if admin is authorized to delete specific questionnaire
		if (questionnaire.getMedicalChamber() == user.getAdminPersonalData().getMedicalChamber()
				&& questionnaire.getStatus() == Status.SENT) {
			questionnaireService.delete(id);
		}
		
		return "redirect:/chamber/showQuestListForAcceptance";
	}
	
}
