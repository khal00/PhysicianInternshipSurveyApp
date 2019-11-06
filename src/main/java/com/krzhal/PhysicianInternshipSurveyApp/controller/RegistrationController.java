package com.krzhal.PhysicianInternshipSurveyApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

import com.krzhal.PhysicianInternshipSurveyApp.UserFormData.UserFormData;
import com.krzhal.PhysicianInternshipSurveyApp.service.UserService;

@Controller
@RequestMapping("/register")
public class RegistrationController {
	
	@Autowired
	UserService userService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping("/showRegistrationForm")
	public String showRegistrationForm(Model theModel) {
		
		theModel.addAttribute("userFormData", new UserFormData());
		
		return "registration-form";
	}

}
