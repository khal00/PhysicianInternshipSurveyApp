package com.khal.intern_survey.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khal.intern_survey.entity.User;
import com.khal.intern_survey.entity.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
	
	VerificationToken findByToken(String token);
	
	VerificationToken findByUser(User user);

}
