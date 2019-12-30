package com.khal.intern_survey.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping("/fillQuestionnaire")
	public String showQuestionnaire(Principal principal, Model theModel) {
		
		User user = userService.findByEmail(principal.getName());
		Questionnaire questionnaire = user.getQuestionnaire();

		theModel.addAttribute("questionnaire", questionnaire);
		
		return "questionnaire_view";
	}

}
