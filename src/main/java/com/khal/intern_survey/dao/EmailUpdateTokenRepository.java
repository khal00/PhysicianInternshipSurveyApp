package com.khal.intern_survey.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khal.intern_survey.entity.EmailUpdateToken;
import com.khal.intern_survey.entity.VerificationToken;

public interface EmailUpdateTokenRepository extends JpaRepository<EmailUpdateToken, Long>{
	
	EmailUpdateToken findByToken(String token);

}
