package com.khal.intern_survey.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khal.intern_survey.DTO.PasswordDTO;
import com.khal.intern_survey.dao.PasswordResetTokenRepository;
import com.khal.intern_survey.entity.PasswordResetToken;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.UserService;

@Controller
public class LoginController {
	
	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	@Autowired
	private UserService userService;

	@Autowired
	MessageSource messages;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@GetMapping("/showLoginForm")
	public String showLoginForm() {
		return "login";
	}

	// Reset password

	@GetMapping("/forgotPassword")
	public String showResetPasswordForm() {

		return "reset_password";
	}

	@PostMapping("/resetPassword")
	public String resetPassword(HttpServletRequest request, @RequestParam("username") String userEmail,
			RedirectAttributes redirectAttributes) {

		Locale locale = LocaleContextHolder.getLocale();
		String appUrl = getBaseUrl(request);

		User user = userService.findByEmail(userEmail);
		if (user == null) {
			String message = messages.getMessage("reset.invaliduser", null, locale);
			redirectAttributes.addFlashAttribute("resetInvalidUserMessage", message);
			return "redirect:/forgotPassword";
		}

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		mailSender.send(constructResetTokenEmail(appUrl, locale, token, user));
		String message = messages.getMessage("reset.emailsent", null, locale);
		redirectAttributes.addFlashAttribute("successMessage", message);
		return "redirect:/";
	}

	// Construct email to reset password
	private SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user) {
		String url = contextPath + "/changePassword?id=" + user.getId() + "&token=" + token;
		String subject = messages.getMessage("reset.mailsubject", null, locale);
		String message = messages.getMessage("reset.mailbody", null, locale);
		return constructEmail(subject, message + " \r\n" + url, user);
	}

	private SimpleMailMessage constructEmail(String subject, String body, User user) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		return email;
	}

	@GetMapping("/changePassword")
	public String showChangePasswordPage(Locale locale, RedirectAttributes redirectAttributes, @RequestParam("id") long id,
			@RequestParam("token") String token, Model theModel) {
		
		String result = validatePasswordResetToken(id, token);
		if (result != null) {
			redirectAttributes.addFlashAttribute("message", messages.getMessage("reset." + result, null, locale));
			return "redirect:/?lang=" + locale.getLanguage();
		}
		
		theModel.addAttribute("passwordDTO", new PasswordDTO());
		return "update_password";
	}

	public String validatePasswordResetToken(long id, String token) {
		PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
		if ((passwordResetToken == null) || (passwordResetToken.getUser().getId() != id)) {
			return "invalidtoken";
		}

		Calendar cal = Calendar.getInstance();
		if ((passwordResetToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return "expired";
		}

		User user = passwordResetToken.getUser();
		
		Authentication auth = new UsernamePasswordAuthenticationToken(user, null,
				Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
		SecurityContextHolder.getContext().setAuthentication(auth);

		return null;
	}
	
	@PostMapping("/savePassword")
	public String savePassword(@Valid @ModelAttribute ("passwordDTO") PasswordDTO passwordDTO
			, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
	
		if(bindingResult.hasErrors()) {
			return "update_password";
		}
		
	    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    userService.changeUserPassword(user, passwordDTO.getPassword());
	
	    Locale locale = LocaleContextHolder.getLocale();
	    redirectAttributes.addFlashAttribute("password_changed_message", messages.getMessage("reset.passwordsaved", null, locale));
		return "redirect:/showLoginForm";
	}
	

	@GetMapping("/access-denied")
	public String showAccessDenied() {

		return "access-denied";

	}

	public String getBaseUrl(HttpServletRequest request) {
		String scheme = request.getScheme() + "://";
		String serverName = request.getServerName();
		String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
		String contextPath = request.getContextPath();
		return scheme + serverName + serverPort + contextPath;
	}
	
	

}