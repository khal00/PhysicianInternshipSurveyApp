package com.krzhal.PhysicianInternshipSurveyApp.service;

import java.util.List;

import com.krzhal.PhysicianInternshipSurveyApp.UserFormData.UserFormData;
import com.krzhal.PhysicianInternshipSurveyApp.entity.User;

public interface UserService {
	
	public List<User> findAll();
	
	public void saveUser(UserFormData userFormData);

}
