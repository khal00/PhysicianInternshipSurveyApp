package com.khal.intern_survey.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@GetMapping("/showUserPanel")
	public String showUserPanel() {

		return "user_panel";
	}
	
	@GetMapping("/accountSettings")
	public String showAccountSettings() {
		return "account_settings";
	}
	
	
}
