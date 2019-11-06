package com.krzhal.PhysicianInternshipSurveyApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.krzhal.PhysicianInternshipSurveyApp.UserFormData.UserFormData;
import com.krzhal.PhysicianInternshipSurveyApp.dao.UserRepository;
import com.krzhal.PhysicianInternshipSurveyApp.entity.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	

	@Override
	public List<User> findAll() {
		
		return userRepository.findAll();
	}
	
	

	@Override
	public void saveUser(UserFormData userFormData) {
		
		User theUser = new User();
		userRepository.save(theUser);
	}

}
