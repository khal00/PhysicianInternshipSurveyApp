package com.khal.intern_survey.controller;

import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khal.intern_survey.DTO.UserDTO;
import com.khal.intern_survey.entity.AdminPersonalData;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.entity.VerificationToken;
import com.khal.intern_survey.registration.OnRegistrationCompleteEvent;
import com.khal.intern_survey.service.UserService;
import com.khal.intern_survey.util.UtilMethods;

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
	
	// Flag for medical chamber admin registration form to expand in index page
	private boolean expandAdminForm = false;
	
	@GetMapping("/")
	public String showIndex(Model theModel) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (!(auth instanceof AnonymousAuthenticationToken)) {

		    return ("redirect:/showUserPanel");
		}
		
		if (!theModel.containsAttribute("userDTO")) {
			theModel.addAttribute("userDTO", new UserDTO());
		}
		
		if (!theModel.containsAttribute("adminData")) {
			theModel.addAttribute("adminData", new AdminPersonalData());
		}
				
		theModel.addAttribute("expandAdminForm", expandAdminForm);
		
		return "index";
		
	}
	
	@PostMapping("/processRegistrationForm")
	public String processRegistrationForm(@RequestParam(value = "checkboxForAdminForm", required = false) String checkboxAdminValue
			, @Valid @ModelAttribute("userDTO") UserDTO userDTO
			, BindingResult userBindingResult
			, @Valid @ModelAttribute("adminData") AdminPersonalData adminData
			, BindingResult adminBindingResult
			, HttpServletRequest request
			, RedirectAttributes redirectAttributes) {
			
		String email = userDTO.getEmail();
		String appUrl = UtilMethods.getBaseUrl(request);
		Locale locale = LocaleContextHolder.getLocale();
		
		// check if user selected admin form
		if(checkboxAdminValue != null) {
			expandAdminForm = true;
		} else {
			expandAdminForm = false;
		}
		
		
		// form validation

		if(adminBindingResult.hasErrors() && expandAdminForm == true) {

			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userDTO", userBindingResult);
			redirectAttributes.addFlashAttribute("userDTO", userDTO);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.adminData", adminBindingResult);
			redirectAttributes.addFlashAttribute("adminData", adminData);
			return "redirect:/?lang=" + request.getLocale().getLanguage();
		}

		
		if (userBindingResult.hasErrors()){
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userDTO", userBindingResult);
			redirectAttributes.addFlashAttribute("userDTO", userDTO);
			return "redirect:/?lang=" + request.getLocale().getLanguage();
	    }
		
		// check if email exists in db
		User existing = userService.findByEmail(email);
		if (existing != null) {
			redirectAttributes.addFlashAttribute("registrationError", "User already exists.");
			return "redirect:/?lang=" + request.getLocale().getLanguage();
		}
		
		String registrationSuccessMessage = messages.getMessage("index.registersuccess", null, locale);
		
		// create admin account if user requested medical chamber admin role
		// publish registration event
		if(checkboxAdminValue != null) {
			userService.saveUserAndAdminData(userDTO, adminData);
			
			User registeredUser = userService.findByEmail(email);
			
			try {
		        eventPublisher.publishEvent(new OnRegistrationCompleteEvent (registeredUser, locale, appUrl));
		    } catch (Exception e) {
		    	String errorMessage = messages.getMessage("index.emailerror", null, locale);
		        redirectAttributes.addFlashAttribute("message", errorMessage);
		        return "redirect:/?lang=" + request.getLocale().getLanguage();
		    }
			
			redirectAttributes.addFlashAttribute("successMessage", registrationSuccessMessage);
			return "redirect:/?lang=" + request.getLocale().getLanguage();
		}
		
		// create regular user account
		userService.saveUser(userDTO);
		
		User registeredUser = userService.findByEmail(email);
		
		try {	   
	        eventPublisher.publishEvent(new OnRegistrationCompleteEvent (registeredUser, locale, appUrl));
	    } catch (Exception e) {
	    	String errorMessage = messages.getMessage("index.emailerror", null, locale);
	        redirectAttributes.addFlashAttribute("message", errorMessage);
	        return "redirect:/?lang=" + request.getLocale().getLanguage();
	    }
		
		redirectAttributes.addFlashAttribute("successMessage", registrationSuccessMessage);
		return "redirect:/?lang=" + request.getLocale().getLanguage();
		 
	}
	
	// registration confirmed by email
	@GetMapping("/registrationConfirm")
	public String confirmRegistration (WebRequest request, @RequestParam("token") String token
			, RedirectAttributes redirectAttributes) {
	  
	    Locale locale = request.getLocale();
	     
	    VerificationToken verificationToken = userService.getVerificationToken(token);
	    if (verificationToken == null) {
	        String message = messages.getMessage("accountactivationmessage.invalidToken", null, locale);
	        redirectAttributes.addFlashAttribute("message", message);
	        return "redirect:/?lang=" + locale.getLanguage();
	    }
	         
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	        String message = messages.getMessage("accountactivationmessage.expired", null, locale);
	        redirectAttributes.addFlashAttribute("message", message);
	        return "redirect:/?lang=" + locale.getLanguage();
	    } 
	    
	    // activate user account
	    User user = verificationToken.getUser();
	    
	    user.setEnabled(true); 
	    userService.saveRegisteredUser(user);
	    String message = messages.getMessage("accountactivationmessage.success", null, locale);
	    redirectAttributes.addFlashAttribute("successMessage", message);
	    return "redirect:/?lang=" + request.getLocale().getLanguage();	
	}

}
