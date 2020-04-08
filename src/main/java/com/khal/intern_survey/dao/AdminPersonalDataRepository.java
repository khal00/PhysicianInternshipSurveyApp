package com.khal.intern_survey.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khal.intern_survey.entity.AdminPersonalData;
import com.khal.intern_survey.enums.MedicalChamberEnum;

public interface AdminPersonalDataRepository extends JpaRepository<AdminPersonalData, Integer> {

	List<AdminPersonalData> findByMedicalChamber(MedicalChamberEnum medicalChamber);
	
}
