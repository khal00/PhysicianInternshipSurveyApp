package com.krzhal.PhysicianInternshipSurveyApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krzhal.PhysicianInternshipSurveyApp.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String>{

}
