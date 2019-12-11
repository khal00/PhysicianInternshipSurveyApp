package com.khal.intern_survey.service;

import com.khal.intern_survey.DTO.UserDTO;

public interface EmailService {
	
	public void sendAdminRegistrationRequestAlert(String to, UserDTO userRequestingRole);
}
