package com.krzhal.PhysicianInternshipSurveyApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krzhal.PhysicianInternshipSurveyApp.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUsername(String username);
}
