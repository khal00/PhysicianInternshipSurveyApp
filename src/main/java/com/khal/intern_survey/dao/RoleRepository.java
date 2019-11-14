package com.khal.intern_survey.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khal.intern_survey.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
	
	public Role findByName(String name);

}
