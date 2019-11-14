package com.khal.intern_survey.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.khal.intern_survey.UserDTO.UserDTO;
import com.khal.intern_survey.entity.User;

public interface UserService extends UserDetailsService{
	
	public List<User> findAll();
	
	public void saveUser(UserDTO userDTO);
	
	public User findByEmail(String username);

}
