package com.krzhal.PhysicianInternshipSurveyApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krzhal.PhysicianInternshipSurveyApp.dao.UserRepository;
import com.krzhal.PhysicianInternshipSurveyApp.entity.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> findAll() {
		
		return userRepository.findAll();
	}

}
