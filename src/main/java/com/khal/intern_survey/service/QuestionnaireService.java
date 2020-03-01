package com.khal.intern_survey.service;

import java.util.List;

import com.khal.intern_survey.DTO.MedicalChamberEnum;
import com.khal.intern_survey.entity.InternshipUnit;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.entity.Questionnaire.Status;
import com.khal.intern_survey.entity.User;

public interface QuestionnaireService {
	
	public Questionnaire saveQuestionnaire(Questionnaire questionnaire);

	public Questionnaire findById(Long id);

	public void delete(Long id);

	public List<Questionnaire> findAllSentQuestionnairesByMedicalChamber(User adminUser);

	public List<Questionnaire> searchByVerificationId(User adminUser, String id, Status status);

	public List<Questionnaire> findAllAcceptedQuestionnairesByMedicalChamber(MedicalChamberEnum medicalChamber);

	double calculateSingleQuestionnaireSectionsAvg(Questionnaire questionnaire);

	double calculateMultipleQuestionnairesSectionsAvg(List<Questionnaire> questionnaires);

	double calculateSingleQuestionnaireCoursesAvg(Questionnaire questionnaire);

	double calculateMultipleQuestionnairesCoursesAvg(List<Questionnaire> questionnaires);

	public List<Questionnaire> findAllAcceptedQuestionnairesByUnit(InternshipUnit unit);

}
