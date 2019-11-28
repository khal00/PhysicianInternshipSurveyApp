package com.khal.intern_survey.controller;

import java.util.Calendar;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import com.khal.intern_survey.UserDTO.UserDTO;
import com.khal.intern_survey.entity.AdminPersonalData;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.entity.VerificationToken;
import com.khal.intern_survey.registration.OnRegistrationCompleteEvent;
import com.khal.intern_survey.service.UserService;

@Controller
public class RegistrationController {
	
	@Autowired
	private UserService userService;
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	@Autowired
	MessageSource messages;
	
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
			, Model theModel
			, WebRequest request) {
		
		
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
		if(adminBindingResult.hasErrors() && expandAdminForm == true) {

			theModel.addAttribute("expandAdminForm", expandAdminForm);
			return "index";		
		}
		
		// create admin account if user selected to request for admin role and there is no errors
		if(checkboxAdminValue != null) {
			userService.saveUserAndAdminData(userDTO, adminData);
			return "registration-confirmation";
		}
		
		// create user account
		userService.saveUser(userDTO);
		
		User registeredUser = userService.findByEmail(email);
		
		try {
	        String appUrl = request.getContextPath();
	        eventPublisher.publishEvent(new OnRegistrationCompleteEvent
	          (registeredUser, request.getLocale(), appUrl));
	    } catch (Exception e) {
	        return "email_error";
	    }
		
		return "registration-confirmation";
		 
	}
	
	// confirm registration
	@GetMapping("/registrationConfirm")
	public String confirmRegistration
	  (WebRequest request, Model model, @RequestParam("token") String token) {
	  
	    Locale locale = request.getLocale();
	     
	    VerificationToken verificationToken = userService.getVerificationToken(token);
	    if (verificationToken == null) {
	        String message = messages.getMessage("auth.message.invalidToken", null, locale);
	        model.addAttribute("message", message);
	        return "redirect:/badUser.html?lang=" + locale.getLanguage();
	    }
	     
	    User user = verificationToken.getUser();
	    
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	        String messageValue = messages.getMessage("auth.message.expired", null, locale);
	        model.addAttribute("message", messageValue);
	        return "redirect:/badUser.html?lang=" + locale.getLanguage();
	    } 
	     
	    user.setEnabled(true); 
	    userService.saveRegisteredUser(user); 
	    return "redirect:/?lang=" + request.getLocale().getLanguage(); 
	}

}
