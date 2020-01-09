package com.khal.intern_survey.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khal.intern_survey.entity.InternshipUnit;

public interface InternshipUnitRepository extends JpaRepository<InternshipUnit, Long> {
	
	public List<InternshipUnit> findByOrderByNameAsc();
	
	public List<InternshipUnit> findByMedicalChamberOrderByNameAsc(String medicalChamber);
}
