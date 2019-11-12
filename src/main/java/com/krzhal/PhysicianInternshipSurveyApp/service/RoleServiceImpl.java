package com.krzhal.PhysicianInternshipSurveyApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.krzhal.PhysicianInternshipSurveyApp.dao.RoleRepository;
import com.krzhal.PhysicianInternshipSurveyApp.entity.Role;

@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void save(Role theRole) {

		roleRepository.save(theRole);
		
	}
}
