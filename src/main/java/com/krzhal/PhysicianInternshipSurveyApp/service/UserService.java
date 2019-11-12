package com.krzhal.PhysicianInternshipSurveyApp.service;

import java.util.List;

import com.krzhal.PhysicianInternshipSurveyApp.UserDTO.UserDTO;
import com.krzhal.PhysicianInternshipSurveyApp.entity.User;

public interface UserService {
	
	public List<User> findAll();
	
	public void saveUser(UserDTO userDTO);
	
	public User findByUsername(String username);

}
