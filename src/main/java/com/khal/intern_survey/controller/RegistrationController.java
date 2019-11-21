package com.khal.intern_survey.controller;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.khal.intern_survey.UserDTO.UserDTO;
import com.khal.intern_survey.entity.AdminPersonalData;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.AdminPersonalDataService;
import com.khal.intern_survey.service.UserService;

@Controller
public class RegistrationController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AdminPersonalDataService adminPersonalDataService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	// Flag for admin registration form to expand in index page
	private boolean expandAdminForm = false;
	
	
	@GetMapping("/")
	public String showIndex(Model theModel) {
		
		theModel.addAttribute("userDTO", new UserDTO());
		theModel.addAttribute("adminData", new AdminPersonalData());
		theModel.addAttribute("expandAdminForm", expandAdminForm);
		
		return "index";
		
	}
	
	@PostMapping("/processRegistrationForm")
	public String processRegistrationForm(@RequestParam(value = "checkboxForAdminForm", required = false) String checkboxAdminValue
			, @Valid @ModelAttribute("userDTO") UserDTO userDTO
			, BindingResult userBindingResult
			, @Valid @ModelAttribute("adminData") AdminPersonalData adminData
			, BindingResult adminBindingResult
			, Model theModel) {
		
		
		String email = userDTO.getEmail();
		
		if(checkboxAdminValue != null) {
			expandAdminForm = true;
		} else {
			expandAdminForm = false;
		}
		
		
		// form validation
		if (userBindingResult.hasErrors()){
			theModel.addAttribute("expandAdminForm", expandAdminForm);
			return "index";
	    }
		
		// check if email exists in db
		User existing = userService.findByEmail(email);
		if (existing != null) {
			theModel.addAttribute("userDTO", new UserDTO());
			theModel.addAttribute("registrationError", "User already exists.");
			theModel.addAttribute("adminDTO", new AdminPersonalData());
			return "index";
		}
		
		// user requested admin privileges but the form has errors
		if(adminBindingResult.hasErrors()) {

			theModel.addAttribute("expandAdminForm", expandAdminForm);
			return "index";		
		}
		
		// create admin account
		if(checkboxAdminValue != null) {
			userService.saveUserAndAdminData(userDTO, adminData);
			return "registration-confirmation";
		}
		
		// create user account
		userService.saveUser(userDTO);
		
		return "registration-confirmation";
		 
	}

}
