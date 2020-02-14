package com.khal.intern_survey.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khal.intern_survey.DTO.MedicalChamberEnum;
import com.khal.intern_survey.dao.AdminPersonalDataRepository;
import com.khal.intern_survey.entity.AdminPersonalData;

@Service
public class AdminPersonalDataServiceImpl implements AdminPersonalDataService {

	@Autowired
	AdminPersonalDataRepository adminPersonalDataRepository;
	
	@Override
	public void saveAdminData(AdminPersonalData adminPersonalData) {
		
		adminPersonalDataRepository.save(adminPersonalData);
	}

	@Override
	public List<AdminPersonalData> findByMedicalChamber(MedicalChamberEnum medicalChamber) {
		
		List<AdminPersonalData> admins = adminPersonalDataRepository.findByMedicalChamber(medicalChamber);
		return admins;
	}

}
