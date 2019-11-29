package com.khal.intern_survey.controller;

import java.util.Calendar;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
			, WebRequest request
			, RedirectAttributes redirectedAttributes) {
			
		String email = userDTO.getEmail();
		String appUrl = request.getContextPath();
		Locale locale = LocaleContextHolder.getLocale();

		
		// check if user selected admin form
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
		
		// there is no errors in the form
		String registrationSuccessMessage = messages.getMessage("index.registersuccess", null, locale);
		
		// create admin account if user selected request for admin role option
		// publish registration event for sending an email confirmation
		if(checkboxAdminValue != null) {
			userService.saveUserAndAdminData(userDTO, adminData);
			
			User registeredUser = userService.findByEmail(email);
			
			try {
		        eventPublisher.publishEvent(new OnRegistrationCompleteEvent (registeredUser, locale, appUrl));
		    } catch (Exception e) {
		    	String errorMessage = messages.getMessage("index.emailerror", null, locale);
		        redirectedAttributes.addFlashAttribute("message", errorMessage);
		    	return "redirect:/";
		    }
			
			redirectedAttributes.addFlashAttribute("successMessage", registrationSuccessMessage);
			return "redirect:/";
		}
		
		// create user account
		userService.saveUser(userDTO);
		
		User registeredUser = userService.findByEmail(email);
		
		try {	   
	        eventPublisher.publishEvent(new OnRegistrationCompleteEvent (registeredUser, locale, appUrl));
	    } catch (Exception e) {
	    	String errorMessage = messages.getMessage("index.emailerror", null, locale);
	        redirectedAttributes.addFlashAttribute("message", errorMessage);
	    	return "redirect:/";
	    }
		
		redirectedAttributes.addFlashAttribute("successMessage", registrationSuccessMessage);
		return "redirect:/";
		 
	}
	
	// registration confirmed by email
	@GetMapping("/registrationConfirm")
	public String confirmRegistration (WebRequest request, Model model, @RequestParam("token") String token
			, RedirectAttributes redirectedAttributes) {
	  
	    Locale locale = request.getLocale();
	     
	    VerificationToken verificationToken = userService.getVerificationToken(token);
	    if (verificationToken == null) {
	        String message = messages.getMessage("accountactivationmessage.invalidToken", null, locale);
	        redirectedAttributes.addFlashAttribute("message", message);
	        return "redirect:/?lang=" + locale.getLanguage();
	    }
	     
	    User user = verificationToken.getUser();
	    
	    Calendar cal = Calendar.getInstance();
	    if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	        String message = messages.getMessage("accountactivationmessage.expired", null, locale);
	        redirectedAttributes.addFlashAttribute("message", message);
	        return "redirect:/?lang=" + locale.getLanguage();
	    } 
	    
	    // activate user account
	    user.setEnabled(true); 
	    userService.saveRegisteredUser(user);
	    String message = messages.getMessage("accountactivationmessage.success", null, locale);
	    redirectedAttributes.addFlashAttribute("successMessage", message);
	    return "redirect:/?lang=" + request.getLocale().getLanguage(); 
	
	}
	
	

}
