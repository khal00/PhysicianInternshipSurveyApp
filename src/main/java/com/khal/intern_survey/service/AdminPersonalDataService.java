package com.khal.intern_survey.service;

import java.util.List;

import com.khal.intern_survey.entity.AdminPersonalData;
import com.khal.intern_survey.enums.MedicalChamberEnum;

public interface AdminPersonalDataService {
	
	public void saveAdminData(AdminPersonalData adminPersonalData);

	public List<AdminPersonalData> findByMedicalChamber(MedicalChamberEnum medicalChamber);

}
