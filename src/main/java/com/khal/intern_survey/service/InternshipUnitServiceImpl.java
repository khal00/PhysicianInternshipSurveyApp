package com.khal.intern_survey.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.khal.intern_survey.dao.InternshipUnitRepository;
import com.khal.intern_survey.dto.MedicalChamberEnum;
import com.khal.intern_survey.entity.InternshipUnit;

@Service
public class InternshipUnitServiceImpl implements InternshipUnitService {
	
	@Autowired
	InternshipUnitRepository internshipUnitRepository;
	
	@Override
	public InternshipUnit findByName(String name) {

		return internshipUnitRepository.findByName(name);
	}

	@Override
	public List<InternshipUnit> findAll() {
		
		List<InternshipUnit> units = internshipUnitRepository.findByOrderByNameAsc();
		return units;
	}

	@Override
	public List<InternshipUnit> findByMedicalChamber(MedicalChamberEnum medicalChamber) {
		
		List<InternshipUnit> units = internshipUnitRepository.findByMedicalChamberOrderByNameAsc(medicalChamber);
		return units;
	}
	
	@Override
	public InternshipUnit findById(Long id) {
		
		Optional<InternshipUnit> unitOpt = internshipUnitRepository.findById(id);
		InternshipUnit unit = (unitOpt.isPresent()) ? unitOpt.get() : null;
		return unit;
	}

	@PreAuthorize(value = "hasPermission(#unit.getId(), 'com.khal.intern_survey.entity.InternshipUnit', 'WRITE')")
	@Override
	public void editUnit(InternshipUnit unit, String newName) {
		
		unit.setName(newName);
		internshipUnitRepository.save(unit);		
	}

	@PreAuthorize(value = "hasPermission(#id, 'com.khal.intern_survey.entity.InternshipUnit', 'DELETE')")
	@Override
	public InternshipUnit delete(Long id) {
		
		InternshipUnit unit = this.findById(id);
		internshipUnitRepository.deleteById(id);	
		return unit;
	}
	
	// New transaction is required for acl to work
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public InternshipUnit addUnit(InternshipUnit unit) {
		
		internshipUnitRepository.save(unit);
		return unit;
	}
	

}
