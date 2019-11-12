package com.krzhal.PhysicianInternshipSurveyApp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.krzhal.PhysicianInternshipSurveyApp.UserDTO.UserDTO;
import com.krzhal.PhysicianInternshipSurveyApp.dao.UserRepository;
import com.krzhal.PhysicianInternshipSurveyApp.entity.Role;
import com.krzhal.PhysicianInternshipSurveyApp.entity.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleService roleService;
	
	
	@Override
	public List<User> findAll() {
		
		return userRepository.findAll();
	}
	
	@Override
	public void saveUser(UserDTO userDTO) {
		
		User theUser = new User();
		theUser.setUsername(userDTO.getUsername());
		theUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		
		// set user role for User
		Role theRole = new Role(userDTO.getUsername(), "ROLE_USER");
		
//		System.out.println("NEW ROLE" + theRole);
//		System.out.println("NEW USER" + theUser);
		
		theUser.setRole(theRole);
		
//		System.out.println("NEW ROLE" + theRole);
//		System.out.println("NEW USER" + theUser);
	
		roleService.save(theRole);
		
//		System.out.println("NEW ROLE" + theRole);
//		System.out.println("NEW USER" + theUser);
		
		
		
		userRepository.save(theUser);
	}

	@Override
	public User findByUsername(String username) {
		
		User theUser = userRepository.findByUsername(username);
		return theUser;
	}

}
