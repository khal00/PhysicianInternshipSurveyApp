package com.krzhal.PhysicianInternshipSurveyApp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.krzhal.PhysicianInternshipSurveyApp.UserFormData.UserFormData;
import com.krzhal.PhysicianInternshipSurveyApp.entity.User;
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
	
	@PostMapping("/processRegistrationForm")
	public String processRegistrationForm(@Valid @ModelAttribute("userFormData") UserFormData userFormData
			, BindingResult theBindingResult
			, Model theModel) {
		
		String username = userFormData.getUsername();
		
		// form validation
		if (theBindingResult.hasErrors()){
			 return "registration-form";
	    }
		
		// check if username exists in db
		User existing = userService.findByUsername(username);
		if (existing != null) {
			theModel.addAttribute("crmUser", new UserFormData());
			theModel.addAttribute("registrationError", "User name already exists.");
			return "registration-form";
		}
		
		//create user account
		userService.saveUser(userFormData);
		
		return "registration-confirmation";
		 
	}

}
