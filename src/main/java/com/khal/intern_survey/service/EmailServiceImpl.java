package com.khal.intern_survey.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.khal.intern_survey.DTO.UserDTO;
import com.khal.intern_survey.entity.User;

@Component
public class EmailServiceImpl implements EmailService{
	
	@Autowired
    public JavaMailSender emailSender;

	@Autowired
	MessageSource messages;
	
	@Override
	public void sendAdminRegistrationRequestAlert(String to, UserDTO userRequestingRole) {
		
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(to);
		email.setSubject("Internship Survey App: Request for Administrator role");
		email.setText("User " + userRequestingRole.getEmail() + " requested for Administrator Role");
		emailSender.send(email);
	}
	
	@Override
	public void sendRegistrationVerificationEmail(String to, String confirmationUrl, Locale locale) {
		
		String subject = messages.getMessage("accountactivationemail.subject", null, locale);
		String text = messages.getMessage("accountactivationemail.text", null, locale);
		
	    SimpleMailMessage email = new SimpleMailMessage();
	    email.setTo(to);
	    email.setSubject(subject);
	    email.setText(text + " \r\n" + confirmationUrl);
	    emailSender.send(email);
		
	}
	
	@Override
	public void sendResetPasswordTokenEmail(String contextPath, Locale locale, String token, User user) {
		
		String url = contextPath + "/changePassword?id=" + user.getId() + "&token=" + token;
		String subject = messages.getMessage("reset.mailsubject", null, locale);
		String text = messages.getMessage("reset.mailbody", null, locale);
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(text + " \r\n" + url);
		email.setTo(user.getEmail());
		emailSender.send(email);
	}

	@Override
	public void sendEmailUpdateVerificationToken(String appUrl, Locale locale, String token, String newEmail) {
		
		String url = appUrl + "/user/confirmNewEmail?token=" + token;
		String subject = messages.getMessage("emailupdate.mailsubject", null, locale);
		String text = messages.getMessage("emailupdate.mailbody", null, locale);
		SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject(subject);
		email.setText(text + " \r\n" + url);
		email.setTo(newEmail);
		emailSender.send(email);
	}

}
