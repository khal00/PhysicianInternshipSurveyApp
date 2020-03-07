package com.khal.intern_survey.service;

import java.util.List;

import com.khal.intern_survey.dto.MedicalChamberEnum;
import com.khal.intern_survey.entity.InternshipUnit;

public interface InternshipUnitService {
	
	public InternshipUnit findByName(String name);
	
	public List<InternshipUnit> findAll();
	
	public List<InternshipUnit> findByMedicalChamber(MedicalChamberEnum medicalChamber);

	InternshipUnit findById(Long id);

	public void editUnit(InternshipUnit unit, String newName);

	public InternshipUnit delete(Long id);

	InternshipUnit addUnit(InternshipUnit unit);

}
