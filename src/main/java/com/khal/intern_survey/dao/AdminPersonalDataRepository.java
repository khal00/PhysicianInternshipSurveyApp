package com.khal.intern_survey.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khal.intern_survey.entity.AdminPersonalData;

public interface AdminPersonalDataRepository extends JpaRepository<AdminPersonalData, Integer> {
	
}
