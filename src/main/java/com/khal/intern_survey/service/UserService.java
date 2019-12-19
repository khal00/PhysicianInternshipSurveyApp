package com.khal.intern_survey.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.khal.intern_survey.DTO.UserDTO;
import com.khal.intern_survey.entity.AdminPersonalData;
import com.khal.intern_survey.entity.EmailUpdateToken;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.entity.VerificationToken;

public interface UserService extends UserDetailsService{
	
	public List<User> findAll();
	
	public void saveUser(UserDTO userDTO);
	
	public User findByEmail(String username);
	
	public void saveUserAndAdminData(UserDTO userDTO, AdminPersonalData adminPersonalData);
	
	public User getUser(String verificationToken);
	 
	public void saveRegisteredUser(User user);
 
	public void createVerificationToken(User user, String token);
 
	public VerificationToken getVerificationToken(String token);
	
	public void createPasswordResetTokenForUser(User user, String token);

	public void changeUserPassword(User user, String password);
	
	public void createEmailUpdateToken(String token, User user, String newEmail);
	
	public EmailUpdateToken getEmailUpdateToken(String token);
	
	public void updateUserEmail(EmailUpdateToken emailUpdateToken);

}
