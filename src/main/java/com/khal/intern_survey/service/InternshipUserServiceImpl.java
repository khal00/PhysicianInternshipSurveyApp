package com.khal.intern_survey.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.khal.intern_survey.dao.InternshipUnitRepository;
import com.khal.intern_survey.entity.InternshipUnit;

public class InternshipUserServiceImpl implements InternshipUnitService {
	
	@Autowired
	InternshipUnitRepository internshipUnitRepository;

	@Override
	public List<InternshipUnit> findAll() {
		
		List<InternshipUnit> units = internshipUnitRepository.findByOrderByNameAsc();
		return units;
	}

}
