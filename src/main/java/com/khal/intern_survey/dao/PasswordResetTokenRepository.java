package com.khal.intern_survey.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khal.intern_survey.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	PasswordResetToken findByToken(String token);

}
