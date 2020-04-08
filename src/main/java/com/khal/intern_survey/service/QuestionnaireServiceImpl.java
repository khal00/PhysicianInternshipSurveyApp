package com.khal.intern_survey.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.khal.intern_survey.dao.QuestionnaireRepository;
import com.khal.intern_survey.entity.Course;
import com.khal.intern_survey.entity.InternshipSection;
import com.khal.intern_survey.entity.InternshipUnit;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.entity.Questionnaire.Status;
import com.khal.intern_survey.enums.CourseEnum;
import com.khal.intern_survey.enums.InternshipSectionsEnum;
import com.khal.intern_survey.enums.MedicalChamberEnum;
import com.khal.intern_survey.entity.User;

@Service
public class QuestionnaireServiceImpl implements QuestionnaireService {
	
	@Autowired
	QuestionnaireRepository questionnaireRepository;
	
	@Autowired
	InternshipSectionService internshipSectionService;
	
	@Autowired
	CourseService courseService;
	
	@Override
	public Questionnaire saveQuestionnaire(Questionnaire questionnaire) {
		
		return questionnaireRepository.save(questionnaire);
		
	}

	@PostAuthorize(value = "hasPermission(returnObject, 'READ')")
	@Override
	public Questionnaire findById(Long id) {
		
		Optional<Questionnaire> questionnaire = questionnaireRepository.findById(id);
		return questionnaire.get();
	}
	
	@PreAuthorize(value = "hasPermission(#id, 'com.khal.intern_survey.entity.Questionnaire', 'DELETE')")
	@Override
	public Questionnaire delete(Long id) {
		
		Questionnaire questionnaire = this.findById(id);
		questionnaireRepository.deleteById(id);
		return questionnaire;
	}

	@Override
	public List<Questionnaire> findAllSentQuestionnairesByMedicalChamber(User adminUser) {
		
		Status status = Status.SENT;
		MedicalChamberEnum medicalChamber = adminUser.getAdminPersonalData().getMedicalChamber();
		
		List<Questionnaire> questionnaires = questionnaireRepository.findByStatusAndMedicalChamberOrderByIdAsc(status, medicalChamber);
		return questionnaires;
	}

	@Override
	public List<Questionnaire> searchByVerificationId(User adminUser, String id, Status status) {
		
		MedicalChamberEnum medicalChamber = adminUser.getAdminPersonalData().getMedicalChamber();
		
		List<Questionnaire> questionnaires = questionnaireRepository
				.findByStatusAndMedicalChamberAndVerificationIdContainingOrderByIdAsc(status, medicalChamber, id);
		return questionnaires;
	}

	@Override
	public List<Questionnaire> findAllAcceptedQuestionnairesByMedicalChamber(MedicalChamberEnum medicalChamber) {
		
		Status status = Status.ACCEPTED;
		List<Questionnaire> questionnaires = questionnaireRepository.findByStatusAndMedicalChamberOrderByIdAsc(status, medicalChamber);
		return questionnaires;
	}
	
	@Override
	public List<Questionnaire> findAllAcceptedQuestionnairesByUnit(InternshipUnit unit) {
		
		Status status = Status.ACCEPTED;
		List<Questionnaire> questionnaires = questionnaireRepository.findByStatusAndUnit(status, unit);
		return questionnaires;
	}
	
	@Override
	public double calculateQuestionnaireAvg(Questionnaire questionnaire) {
		double sum = questionnaire.getCoordinator();
		
		AtomicInteger divider = new AtomicInteger(1);
		double result = 0;
		
		for(InternshipSection section : questionnaire.getSections()) {
			if (!section.isDisabled()) {
				double rating = internshipSectionService.calculateSectionAvg(section.getName().getName(), questionnaire.getId());
				sum += rating;
				divider.incrementAndGet();
				
			}
		
			result = sum / divider.get();
		}
		
		return result;
	}
	
	@Override
	public double calculateQuestionnairesAvg(List<Questionnaire> questionnaires) {
		
		AtomicInteger divider = new AtomicInteger(0);
		double sum = 0;
		double result = 0;
		
		if (!questionnaires.isEmpty()) {
			for(Questionnaire questionnaire : questionnaires) {
				
				sum += calculateQuestionnaireAvg(questionnaire);
				divider.incrementAndGet();				
			}
			
			result = sum / divider.get();			
		}
		
		return result;
	}
	
	@Override
	public double calculateCourseAvg(CourseEnum course, Questionnaire questionnaire) {
		double result = courseService.calculateCourseAvg(course.getName(), questionnaire.getId());
		return result;	
	}
	
	@Override
	public double calculateCourseAvg(CourseEnum course, List<Questionnaire> questionnaires) {
		
		AtomicInteger divider = new AtomicInteger(0);
		double sum = 0;
		double result = 0;
		
		if (!questionnaires.isEmpty()) {
			for(Questionnaire questionnaire : questionnaires) {
				
				double avg = calculateCourseAvg(course, questionnaire);
				sum += avg;
				if (avg > 0) divider.incrementAndGet();
			}
			
			if (sum > 0) result = sum / divider.get();			
		}
		
		return result;
	}
	
	@Override
	public double calculateSectionAvg(InternshipSectionsEnum section, Questionnaire questionnaire) {
		double result = internshipSectionService.calculateSectionAvg(section.getName(), questionnaire.getId());
		return result;	
	}
	
	@Override
	public double calculateSectionAvg(InternshipSectionsEnum section, List<Questionnaire> questionnaires) {
		
		AtomicInteger divider = new AtomicInteger(0);
		double sum = 0;
		double result = 0;
		
		if (!questionnaires.isEmpty()) {
			for(Questionnaire questionnaire : questionnaires) {
				
				double avg = calculateSectionAvg(section, questionnaire);
				sum += avg;
				if (avg > 0) divider.incrementAndGet();
			}
			
			if (sum > 0) result = sum / divider.get();			
		}
		
		return result;
	}
	
	@Override
	public int getSectionNumberOfInterns(InternshipSectionsEnum section, List<Questionnaire> questionnaires) {
		
		AtomicInteger result = new AtomicInteger(0);
		
		if (!questionnaires.isEmpty()) {
			for(Questionnaire questionnaire : questionnaires) {
				if (calculateSectionAvg(section, questionnaire) > 0) {
					result.incrementAndGet();
				}
			}
		}
		return result.get();
	}
	
	@Override
	public int getCourseNumberOfInterns(CourseEnum course, List<Questionnaire> questionnaires) {
		
		AtomicInteger result = new AtomicInteger(0);
		
		if (!questionnaires.isEmpty()) {
			for(Questionnaire questionnaire : questionnaires) {
				if (calculateCourseAvg(course, questionnaire) > 0) {
					result.incrementAndGet();
				}
			}
		}
		return result.get();
	}
}
