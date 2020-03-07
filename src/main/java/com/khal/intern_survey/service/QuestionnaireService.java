package com.khal.intern_survey.service;

import java.util.List;

import com.khal.intern_survey.dto.CourseEnum;
import com.khal.intern_survey.dto.InternshipSectionsEnum;
import com.khal.intern_survey.dto.MedicalChamberEnum;
import com.khal.intern_survey.entity.InternshipUnit;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.entity.Questionnaire.Status;
import com.khal.intern_survey.entity.User;

public interface QuestionnaireService {
	
	public Questionnaire saveQuestionnaire(Questionnaire questionnaire);

	public Questionnaire findById(Long id);

	public Questionnaire delete(Long id);

	public List<Questionnaire> findAllSentQuestionnairesByMedicalChamber(User adminUser);

	public List<Questionnaire> searchByVerificationId(User adminUser, String id, Status status);

	public List<Questionnaire> findAllAcceptedQuestionnairesByMedicalChamber(MedicalChamberEnum medicalChamber);

	double calculateQuestionnaireAvg(Questionnaire questionnaire);

	double calculateQuestionnairesAvg(List<Questionnaire> questionnaires);

	double calculateCourseAvg(CourseEnum course, List<Questionnaire> questionnaires);

	public List<Questionnaire> findAllAcceptedQuestionnairesByUnit(InternshipUnit unit);

	double calculateCourseAvg(CourseEnum course, Questionnaire questionnaire);

	double calculateSectionAvg(InternshipSectionsEnum section, Questionnaire questionnaire);

	double calculateSectionAvg(InternshipSectionsEnum section, List<Questionnaire> questionnaires);

	int getSectionNumberOfInterns(InternshipSectionsEnum section, List<Questionnaire> questionnaires);

	int getCourseNumberOfInterns(CourseEnum course, List<Questionnaire> questionnaires);

}
