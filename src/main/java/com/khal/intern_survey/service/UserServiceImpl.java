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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khal.intern_survey.dao.EmailUpdateTokenRepository;
import com.khal.intern_survey.dao.PasswordResetTokenRepository;
import com.khal.intern_survey.dao.RoleRepository;
import com.khal.intern_survey.dao.UserRepository;
import com.khal.intern_survey.dao.VerificationTokenRepository;
import com.khal.intern_survey.dto.UserDTO;
import com.khal.intern_survey.entity.AdminPersonalData;
import com.khal.intern_survey.entity.EmailUpdateToken;
import com.khal.intern_survey.entity.PasswordResetToken;
import com.khal.intern_survey.entity.Role;
import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.entity.VerificationToken;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private EmailServiceImpl emailServiceImpl;
	
    @Autowired
    private VerificationTokenRepository tokenRepository;
    
    @Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
    
    @Autowired
    private EmailUpdateTokenRepository emailUpdateTokenRepository;
	
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
	
//	Save Admin user and email system admin to verify request for medical chamber admin role
	
	@Override
	public void saveUserAndAdminData(UserDTO userDTO, AdminPersonalData adminPersonalData) {
		
		User theUser = new User();
		theUser.setEmail(userDTO.getEmail());
		theUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));		
		theUser.setAdminPersonalData(adminPersonalData);
		userRepository.save(theUser);
		emailServiceImpl.sendAdminRegistrationRequestAlert("krzhalewski@gmail.com", userDTO);
	}

	@Override
	public User findByEmail(String email) {
		
		User theUser = userRepository.findByEmail(email);
		return theUser;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
	    boolean accountNonExpired = true;
	    boolean credentialsNonExpired = true;
	    boolean accountNonLocked = true;

		//use email as username
		User user = userRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid email or password.");
		}
		return new org.springframework.security.core.userdetails.User(
				user.getEmail(), 
				user.getPassword(),
		        user.isEnabled(), 
		        accountNonExpired, 
		        credentialsNonExpired, 
		        accountNonLocked, 
				mapRolesToAuthorities(user.getRoles()));
	}
	
	@Override
	public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	public User getUser(String verificationToken) {
        User user = tokenRepository.findByToken(verificationToken).getUser();
        return user;
	}

	@Override
	public void saveRegisteredUser(User user) {
		userRepository.save(user);
	}

	@Override
	public void createVerificationToken(User user, String token) {
		VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
	}

	@Override
	public VerificationToken getVerificationToken(String token) {
		return tokenRepository.findByToken(token);
	}
	
	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
	    PasswordResetToken newToken = new PasswordResetToken(token, user);
	    passwordResetTokenRepository.save(newToken);
	}

	@Override
	public void changeUserPassword(User user, String password) {
		user.setPassword(passwordEncoder.encode(password));
		userRepository.save(user);
	}

	@Override
	public void createEmailUpdateToken(String token, User user, String newEmail) {
		EmailUpdateToken emailUpdateToken = new EmailUpdateToken(token, user, newEmail);
		emailUpdateTokenRepository.save(emailUpdateToken);
		
	}

	@Override
	public EmailUpdateToken getEmailUpdateToken(String token) {
		EmailUpdateToken emailUpdateToken = emailUpdateTokenRepository.findByToken(token);
		
		return emailUpdateToken;
	}

	@Override
	public void updateUserEmail(EmailUpdateToken emailUpdateToken) {
		User user = emailUpdateToken.getUser();
		user.setEmail(emailUpdateToken.getNewEmail());
		userRepository.save(user);
	}
	
	@Override
	public void deleteUserAccount(User user) {
		userRepository.delete(user);
	}
}
