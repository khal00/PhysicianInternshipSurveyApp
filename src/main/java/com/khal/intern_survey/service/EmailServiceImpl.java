package com.khal.intern_survey.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.khal.intern_survey.UserDTO.UserDTO;

@Component
public class EmailServiceImpl implements EmailService{
	
	@Autowired
    public JavaMailSender emailSender;
 
    public void sendAdminRegistrationRequestAlert(
      String to, String subject, String text) {
        
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setTo(to); 
        message.setSubject(subject); 
        message.setText(text);
        emailSender.send(message);
        
    }

	@Override
	public void sendAdminRegistrationRequestAlert(String to, UserDTO userRequestingRole) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Internship Survey App: Request for Admin role");
		message.setText("User " + userRequestingRole.getEmail() + " requested for Administrator Role");
		emailSender.send(message);
	}

}
