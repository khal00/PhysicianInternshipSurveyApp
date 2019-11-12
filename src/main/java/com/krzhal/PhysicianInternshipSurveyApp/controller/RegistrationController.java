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

import com.krzhal.PhysicianInternshipSurveyApp.UserDTO.UserDTO;
import com.krzhal.PhysicianInternshipSurveyApp.entity.Role;
import com.krzhal.PhysicianInternshipSurveyApp.entity.User;
import com.krzhal.PhysicianInternshipSurveyApp.service.RoleService;
import com.krzhal.PhysicianInternshipSurveyApp.service.UserService;

@Controller
@RequestMapping("/register")
public class RegistrationController {
	
	@Autowired
	private UserService userService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@GetMapping("/showRegistrationForm")
	public String showRegistrationForm(Model theModel) {
		
		theModel.addAttribute("userDTO", new UserDTO());
		
		return "registration-form";
	}
	
	@PostMapping("/processRegistrationForm")
	public String processRegistrationForm(@Valid @ModelAttribute("userDTO") UserDTO userDTO
			, BindingResult theBindingResult
			, Model theModel) {
		
		String username = userDTO.getUsername();
		
		// form validation
		if (theBindingResult.hasErrors()){
			 return "registration-form";
	    }
		
		// check if username exists in db
		User existing = userService.findByUsername(username);
		if (existing != null) {
			theModel.addAttribute("userDTO", new UserDTO());
			theModel.addAttribute("registrationError", "User name already exists.");
			return "registration-form";
		}
		
		// create user account
		userService.saveUser(userDTO);
		
		return "registration-confirmation";
		 
	}

}
