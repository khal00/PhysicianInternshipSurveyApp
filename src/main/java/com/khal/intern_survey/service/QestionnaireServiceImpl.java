package com.khal.intern_survey.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import com.khal.intern_survey.DTO.MedicalChamberEnum;
import com.khal.intern_survey.dao.QuestionnaireRepository;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.entity.Questionnaire.Status;
import com.khal.intern_survey.entity.User;

@Service
public class QestionnaireServiceImpl implements QuestionnaireService {
	
	@Autowired
	QuestionnaireRepository questionnaireRepository;

	@Override
	public void saveQuestionnaire(Questionnaire questionnaire) {

		questionnaireRepository.save(questionnaire);
		
	}

	@PostAuthorize("hasPermission(returnObject, 'READ')")
	@Override
	public Questionnaire findById(Long id) {
		
		Optional<Questionnaire> questionnaire = questionnaireRepository.findById(id);
		return questionnaire.get();
	}

	@Override
	public void delete(Long id) {
		questionnaireRepository.deleteById(id);
		
	}

	@Override
	public List<Questionnaire> findByStatusAndMedicalChamber(User adminUser) {
		
		Status status = Status.SENT;
		MedicalChamberEnum medicalChamber = adminUser.getAdminPersonalData().getMedicalChamber();
		
		List<Questionnaire> questionnaires = questionnaireRepository.findByStatusAndMedicalChamberOrderBySendDateAsc(status, medicalChamber);
		return questionnaires;
	}

	@Override
	public List<Questionnaire> searchByVerificationId(User adminUser, String id) {
		
		Status status = Status.SENT;
		MedicalChamberEnum medicalChamber = adminUser.getAdminPersonalData().getMedicalChamber();
		
		List<Questionnaire> questionnaires = questionnaireRepository
				.findByStatusAndMedicalChamberAndVerificationIdContainingOrderBySendDateAsc(status, medicalChamber, id);
		return questionnaires;
	}


	
}
