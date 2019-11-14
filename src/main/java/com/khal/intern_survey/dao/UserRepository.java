package com.khal.intern_survey.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khal.intern_survey.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByEmail(String email);
}
