package com.khal.intern_survey.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.khal.intern_survey.UserDTO.UserDTO;
import com.khal.intern_survey.dao.PasswordResetTokenRepository;
import com.khal.intern_survey.entity.PasswordResetToken;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.service.UserService;

@Controller
public class LoginController {

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

	@GetMapping("/showUserPanel")
	public String showUserPanel() {

		return "user_panel";
	}

	@GetMapping("/forgotPassword")
	public String showResetPasswordForm() {

		return "reset_password";
	}

	@PostMapping("/resetPassword")
	public String resetPassword(WebRequest request, @RequestParam("username") String userEmail,
			RedirectAttributes redirectAttributes) {

		Locale locale = LocaleContextHolder.getLocale();
		String appUrl = request.getContextPath();

//		if (bindingResult.hasErrors()) {
//			return "reset_password";
//		}

		User user = userService.findByEmail(userEmail);
		if (user == null) {
			return "reset_password";
		}

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		mailSender.send(constructResetTokenEmail(appUrl, locale, token, user));
		String message = messages.getMessage("reset.emailsent", null, locale);
		redirectAttributes.addFlashAttribute("resetMessage", message);
		return "redirect:/";
	}

	@GetMapping("/access-denied")
	public String showAccessDenied() {

		return "access-denied";

	}

	// Construct email to reset password
	private SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user) {
		String url = contextPath + "/changePassword?id=" + user.getId() + "&token=" + token;
		String subject = messages.getMessage("reset.mailsubject", null, locale);
		String message = messages.getMessage("reset.mailbody", null, locale);
		return constructEmail(subject , message + " \r\n" + url, user);
	}

	private SimpleMailMessage constructEmail(String subject, String body, User user) {
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(body);
		email.setTo(user.getEmail());
		return email;
	}
	
	@GetMapping("/changePassword")
	public String showChangePasswordPage(Locale locale, Model model, 
	  @RequestParam("id") long id, @RequestParam("token") String token) {
	    String result = validatePasswordResetToken(id, token);
	    if (result != null) {
	        model.addAttribute("message", 
	          messages.getMessage("auth.message." + result, null, locale));
	        return "redirect:/login?lang=" + locale.getLanguage();
	    }
	    return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
	}
	
	public String validatePasswordResetToken(long id, String token) {
	    PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
	    if ((passwordResetToken == null) || (passwordResetToken.getUser().getId() != id)) {
	        return "invalidToken";
	    }
	 
	    Calendar cal = Calendar.getInstance();
	    if ((passwordResetToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
	        return "expired";
	    }
	 
	    User user = passwordResetToken.getUser();
	    Authentication auth = new UsernamePasswordAuthenticationToken(
	      user, null, Arrays.asList(
	      new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
	    SecurityContextHolder.getContext().setAuthentication(auth);
	    return null;
	}

}