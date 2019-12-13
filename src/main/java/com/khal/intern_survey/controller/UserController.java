package com.khal.intern_survey.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/showUserPanel")
	public String showUserPanel() {

		return "user_panel";
	}
	
	@GetMapping("/accountSettings")
	public String showAccountSettings(Principal principal, Model theModel) {
		
		User user = userService.findByEmail(principal.getName());
		theModel.addAttribute("user", user);
		
		return "account_settings";
	}
	
	@PostMapping("/updatePassword")
	public String updatePassword(@RequestParam (value = "email", required = true) String email, Model theModel, Principal principal) {
		
		
		
		
		User user = userService.findByEmail(principal.getName());
		theModel.addAttribute("user", user);
		return "account_settings";
		
	}
	
	
}
