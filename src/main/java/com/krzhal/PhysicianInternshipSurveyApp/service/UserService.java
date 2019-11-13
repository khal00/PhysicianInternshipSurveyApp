package com.krzhal.PhysicianInternshipSurveyApp.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.krzhal.PhysicianInternshipSurveyApp.UserDTO.UserDTO;
import com.krzhal.PhysicianInternshipSurveyApp.entity.User;

public interface UserService extends UserDetailsService{
	
	public List<User> findAll();
	
	public void saveUser(UserDTO userDTO);
	
	public User findByUsername(String username);

}
