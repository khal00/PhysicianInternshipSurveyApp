package com.khal.intern_survey.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.QuestionnaireService;
import com.khal.intern_survey.service.UserService;

@Controller
@RequestMapping("/survey")
public class SurveyController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	QuestionnaireService questionnaireService;
	
	@GetMapping("/showQuestionnaire")
	public String showQuestionnaire(Principal principal, Model theModel) {
		
		User user = userService.findByEmail(principal.getName());
		Questionnaire questionnaire = user.getQuestionnaire();

		if (questionnaire == null) {
			questionnaire = new Questionnaire();
			questionnaireService.saveQuestionnaire(questionnaire);
			user.setQuestionnaire(questionnaire);
			userService.saveRegisteredUser(user);
		}
		
		theModel.addAttribute("questionnaire", questionnaire);

		return "questionnaire_view";
	}
	
	@PostMapping(value = "/saveQuestionnaire", params = "action=save")
	public String saveQuestionnaire(@ModelAttribute ("questionnaire") Questionnaire questionnaire) {
		
		questionnaireService.saveQuestionnaire(questionnaire);
		
		return "redirect:/survey/showQuestionnaire";
	}

	@PostMapping(value = "/saveQuestionnaire", params = "action=send")
	public String sendQuestionnaire(@ModelAttribute ("questionnaire") Questionnaire questionnaire) {
		
//		questionnaireService.saveQuestionnaire(questionnaire);
		
		return "redirect:/survey/showQuestionnaire";
	}
	
}
