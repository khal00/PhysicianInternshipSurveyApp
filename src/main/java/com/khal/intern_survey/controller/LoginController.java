package com.khal.intern_survey.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.khal.intern_survey.UserDTO.UserDTO;

@Controller
public class LoginController {
	

	@GetMapping("/showLoginForm")
	public String showLoginForm() {
		return "login";
	}
	
	@GetMapping("/showUserPanel")
	public String showUserPanel() {
		
		return "user_panel";
		
	}
	
	
	@GetMapping("/access-denied")
	public String showAccessDenied() {
		
		return "access-denied";
		
	}
	
	
	
}