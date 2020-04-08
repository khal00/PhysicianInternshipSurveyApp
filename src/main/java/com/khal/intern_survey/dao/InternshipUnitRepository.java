package com.khal.intern_survey.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khal.intern_survey.entity.InternshipUnit;
import com.khal.intern_survey.enums.MedicalChamberEnum;

public interface InternshipUnitRepository extends JpaRepository<InternshipUnit, Long> {
	
	public InternshipUnit findByName(String name);
	
	public List<InternshipUnit> findByOrderByNameAsc();
	
	public List<InternshipUnit> findByMedicalChamberOrderByNameAsc(MedicalChamberEnum medicalChamber);
}
