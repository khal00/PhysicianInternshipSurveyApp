package com.khal.intern_survey.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khal.intern_survey.dao.InternshipUnitRepository;
import com.khal.intern_survey.entity.InternshipUnit;

@Service
public class InternshipUserServiceImpl implements InternshipUnitService {
	
	@Autowired
	InternshipUnitRepository internshipUnitRepository;

	@Override
	public List<InternshipUnit> findAll() {
		
		List<InternshipUnit> units = internshipUnitRepository.findByOrderByNameAsc();
		return units;
	}

	@Override
	public List<InternshipUnit> findByMedicalChamber(String medicalChamber) {
		
		List<InternshipUnit> units = internshipUnitRepository.findByMedicalChamberOrderByNameAsc(medicalChamber);
		return units;
	}
	
	

}
