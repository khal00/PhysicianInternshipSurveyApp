package com.khal.intern_survey.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
	

	@GetMapping("/showLoginPage")
	public String showMyLoginPage() {
		
		return "login";
		
	}

	
	@GetMapping("/access-denied")
	public String showAccessDenied() {
		
		return "access-denied";
		
	}
	
}