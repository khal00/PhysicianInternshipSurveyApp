package com.khal.intern_survey.controller;

import java.security.Principal;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.EmailService;
import com.khal.intern_survey.service.UserService;
import com.khal.intern_survey.util.UtilMethods;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	MessageSource messages;
	
	@Autowired
	EmailService emailService;
	
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
	
	@PostMapping("/updateEmail")
	public String updateEmail(HttpServletRequest request, @RequestParam (value = "email", required = true) String email, Model theModel, Principal principal) {

		String appUrl = UtilMethods.getBaseUrl(request);
		Locale locale = LocaleContextHolder.getLocale();
		
		User user = userService.findByEmail(principal.getName());
		
		String token = UUID.randomUUID().toString();
		userService.createVerificationToken(user, token);
        
        emailService.sendEmailUpdateVerificationToken(appUrl, locale, token, user);
        
        
		return "account_settings";
		
	}
	
	
}
