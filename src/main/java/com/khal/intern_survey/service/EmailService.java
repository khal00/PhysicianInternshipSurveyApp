package com.khal.intern_survey.service;

import java.util.Locale;

import com.khal.intern_survey.dto.UserDTO;
import com.khal.intern_survey.entity.User;

public interface EmailService {
	
	public void sendAdminRegistrationRequestAlert(String to, UserDTO userRequestingRole);

	public void sendResetPasswordTokenEmail(String appUrl, Locale locale, String token, User user);

	void sendRegistrationVerificationEmail(String to, String confirmationUrl, Locale locale);

	public void sendEmailUpdateVerificationToken(String appUrl, Locale locale, String token, String newEmail);
}
