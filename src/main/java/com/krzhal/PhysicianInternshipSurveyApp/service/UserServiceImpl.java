package com.krzhal.PhysicianInternshipSurveyApp.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.krzhal.PhysicianInternshipSurveyApp.UserDTO.UserDTO;
import com.krzhal.PhysicianInternshipSurveyApp.dao.RoleRepository;
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
	private RoleRepository roleRepository;
	
	
	@Override
	public List<User> findAll() {
		
		return userRepository.findAll();
	}
	
	@Override
	public void saveUser(UserDTO userDTO) {
		
		User theUser = new User();
		theUser.setUsername(userDTO.getUsername());
		theUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		
		theUser.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
		userRepository.save(theUser);
	}

	@Override
	public User findByUsername(String username) {
		
		User theUser = userRepository.findByUsername(username);
		return theUser;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

}
