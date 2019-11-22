package com.khal.intern_survey.service;

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

import com.khal.intern_survey.UserDTO.UserDTO;
import com.khal.intern_survey.dao.AdminPersonalDataRepository;
import com.khal.intern_survey.dao.RoleRepository;
import com.khal.intern_survey.dao.UserRepository;
import com.khal.intern_survey.entity.AdminPersonalData;
import com.khal.intern_survey.entity.Role;
import com.khal.intern_survey.entity.User;

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
	
//	Save regular user
	
	@Override
	public void saveUser(UserDTO userDTO) {
		
		User theUser = new User();
		theUser.setEmail(userDTO.getEmail());
		theUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		
		theUser.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
		userRepository.save(theUser);
	}
	
//	Save Admin user and send email sys admin to verify request for admin role
	
	@Override
	public void saveUserAndAdminData(UserDTO userDTO, AdminPersonalData adminPersonalData) {
		
		User theUser = new User();
		theUser.setEmail(userDTO.getEmail());
		theUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));		
//		theUser.setRoles(Arrays.asList(roleRepository.findByName("ROLE_OILADMIN")));
		theUser.setAdminPersonalData(adminPersonalData);
		userRepository.save(theUser);
	}

	@Override
	public User findByEmail(String email) {
		
		User theUser = userRepository.findByEmail(email);
		return theUser;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//use email as username
		User user = userRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid email or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}
	
	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}


}
