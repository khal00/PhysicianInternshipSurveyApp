package com.khal.intern_survey.controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khal.intern_survey.DTO.EmailDTO;
import com.khal.intern_survey.DTO.PasswordDTO;
import com.khal.intern_survey.entity.EmailUpdateToken;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.EmailService;
import com.khal.intern_survey.service.UserService;
import com.khal.intern_survey.util.UtilMethods;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageSource messages;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/showUserPanel")
	public String showUserPanel() {

		return "user_panel";
	}
	
	@GetMapping("/accountSettings")
	public String showAccountSettings(Principal principal, Model theModel) {
		
		User user = userService.findByEmail(principal.getName());
		
		if (!theModel.containsAttribute("emailDTO")) {
			theModel.addAttribute("emailDTO", new EmailDTO(user.getEmail()));
		}
		
		if (!theModel.containsAttribute("passwordDTO")) {
			theModel.addAttribute("passwordDTO", new PasswordDTO());
		}
		
		return "account_settings";
	}
	
	
	@PostMapping("/updateEmail")
	public String updateEmail(HttpServletRequest request
			, @Valid @ModelAttribute ("emailDTO") EmailDTO emailDTO
			, BindingResult bindingResult
			, Model theModel
			, Principal principal
			, RedirectAttributes redirectAttributes) {
		
		if(bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.emailDTO", bindingResult);
			redirectAttributes.addFlashAttribute("emailDTO", emailDTO);
			return "redirect:/user/accountSettings";
		}
		
		String newEmail = emailDTO.getEmailAddress();
		
		String appUrl = UtilMethods.getBaseUrl(request);
		Locale locale = LocaleContextHolder.getLocale();
		
		// Check if new email is not already in use
		User tempUser = userService.findByEmail(newEmail);
		if(tempUser != null) {
			String message = messages.getMessage("emailupdate.emailexists", null, locale);
	        redirectAttributes.addFlashAttribute("errorMessage", message);
	        return "redirect:/user/accountSettings";
		}
		
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
	
	@PostMapping("/updatePassword")
	public String updatePassword(Principal principal
			, @RequestParam String oldPass
			, @Valid @ModelAttribute ("passwordDTO") PasswordDTO passwordDTO
			, BindingResult bindingResult
			, RedirectAttributes redirectAttributes) {
		
		Locale locale = LocaleContextHolder.getLocale();
		User user = userService.findByEmail(principal.getName());
		
		
		// Check if password matches
		if(!passwordEncoder.matches(oldPass, user.getPassword())){
			
			String message = messages.getMessage("account.invalidpassword", null, locale);
			
			redirectAttributes.addFlashAttribute("passwordErrorMessage", message);
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordDTO", bindingResult);
			redirectAttributes.addFlashAttribute("passwordDTO", passwordDTO);
			return "redirect:/user/accountSettings";		
		}
		
		if(bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordDTO", bindingResult);
			redirectAttributes.addFlashAttribute("passwordDTO", passwordDTO);
			return "redirect:/user/accountSettings";
		}
		
		userService.changeUserPassword(user, passwordDTO.getPassword());
		String message = messages.getMessage("account.passwordChageSucc", null, locale);
		redirectAttributes.addFlashAttribute("successMessage", message);
	
		return "redirect:/user/accountSettings";
	}
	
	@PostMapping("/deleteAccount")
	public String deleteAccont(Principal principal, RedirectAttributes redirectAttributes) {
		
		Locale locale = LocaleContextHolder.getLocale();
		
		User user = userService.findByEmail(principal.getName());
		userService.deleteUserAccount(user);
		SecurityContextHolder.clearContext();
		
		String message = messages.getMessage("account.accountDeletedMessage", null, locale);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/";
	}
	
	
	
}
