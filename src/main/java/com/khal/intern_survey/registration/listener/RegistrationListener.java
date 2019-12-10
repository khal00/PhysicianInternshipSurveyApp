package com.khal.intern_survey.registration.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.registration.OnRegistrationCompleteEvent;
import com.khal.intern_survey.service.UserService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
	
	@Autowired
    private UserService userService;
  
    @Autowired
    private MessageSource messages;
  
    @Autowired
    private JavaMailSender mailSender;
 
    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }
 
    
    private void confirmRegistration(OnRegistrationCompleteEvent event) {
    	
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = messages.getMessage("accountactivationemail.subject", null, event.getLocale());
        String confirmationUrl 
          = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = messages.getMessage("accountactivationemail.text", null, event.getLocale());
         
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " " + confirmationUrl);
        mailSender.send(email);
    }
	
}
