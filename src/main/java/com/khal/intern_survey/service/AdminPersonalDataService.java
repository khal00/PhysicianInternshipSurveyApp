package com.khal.intern_survey.service;

import java.util.List;

import com.khal.intern_survey.dto.MedicalChamberEnum;
import com.khal.intern_survey.entity.AdminPersonalData;

public interface AdminPersonalDataService {
	
	public void saveAdminData(AdminPersonalData adminPersonalData);

	public List<AdminPersonalData> findByMedicalChamber(MedicalChamberEnum medicalChamber);

}
