package com.khal.intern_survey.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khal.intern_survey.entity.EmailUpdateToken;
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
		System.out.println("Principal: " + principal);
		System.out.println("Principal.getName: " + principal.getName());
		
		User user = userService.findByEmail(principal.getName());
		theModel.addAttribute("user", user);
		
		return "account_settings";
	}
	
	@PostMapping("/updateEmail")
	public String updateEmail(HttpServletRequest request, @RequestParam (value = "email", required = true) String newEmail
			, Model theModel
			, Principal principal
			, RedirectAttributes redirectAttributes) {

		String appUrl = UtilMethods.getBaseUrl(request);
		Locale locale = LocaleContextHolder.getLocale();
		
		User user = userService.findByEmail(principal.getName());
		
		String token = UUID.randomUUID().toString();
		userService.createEmailUpdateToken(token, user, newEmail);;
        
        emailService.sendEmailUpdateVerificationToken(appUrl, locale, token, newEmail);
        
        String message = messages.getMessage("emailupdate.infomessage", null, locale);
        redirectAttributes.addFlashAttribute("infomessage", message);
        
		return "redirect:/user/accountSettings";
	}
	
	@GetMapping("/confirmNewEmail")
	public String confirmNewEmail(WebRequest request, @RequestParam ("token") String token, RedirectAttributes redirectAttributes) {
		
		Locale locale = request.getLocale();
		EmailUpdateToken emailUpdateToken = userService.getEmailUpdateToken(token);
		
		if (emailUpdateToken == null) {
	        String message = messages.getMessage("emailupdate.invalidToken", null, locale);
	        redirectAttributes.addFlashAttribute("message", message);
	        return "redirect:/?lang=" + locale.getLanguage();
		}
		
	    Calendar cal = Calendar.getInstance();
	    if ((emailUpdateToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	        String message = messages.getMessage("emailupdate.tokenexpired", null, locale);
	        redirectAttributes.addFlashAttribute("message", message);
	        return "redirect:/?lang=" + locale.getLanguage();
	    } 

		
//		Update user email
	    userService.updateUserEmail(emailUpdateToken);
	    
//	    Re-authenticate user to update principal
	    User user = emailUpdateToken.getUser();
	    
	    UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
	    Collection<? extends GrantedAuthority> authorities = userService.mapRolesToAuthorities(user.getRoles());
	    Authentication authentication = new PreAuthenticatedAuthenticationToken(userDetails, user.getPassword(), authorities);
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    
        String message = messages.getMessage("emailupdate.successmessage", null, locale);
        redirectAttributes.addFlashAttribute("successMessage", message);
		
		return "redirect:/?lang=" + locale.getLanguage();
	}
	
	
	
}
