package com.khal.intern_survey.registration.listener;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.registration.OnRegistrationCompleteEvent;
import com.khal.intern_survey.service.EmailService;
import com.khal.intern_survey.service.UserService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
	
	@Autowired
    private UserService userService;
  
    @Autowired
    private EmailService emailService;
 
    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }
 
    
    private void confirmRegistration(OnRegistrationCompleteEvent event) {
    	
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);
        String userAddress = user.getEmail();
        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        
        emailService.sendRegistrationVerificationEmail(userAddress, confirmationUrl, event.getLocale());
    }
	
}
