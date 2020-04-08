package com.khal.intern_survey.service;

import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.khal.intern_survey.dao.CourseRepository;
import com.khal.intern_survey.entity.Course;
import com.khal.intern_survey.entity.InternshipSection;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.enums.CourseEnum;
import com.khal.intern_survey.enums.InternshipSectionsEnum;

@Service
public class CourseServiceImpl implements CourseService {

	@Autowired
	CourseRepository courseRepository;
	
	@Override
	public void saveCourse(Course course) {
		
		courseRepository.save(course);
	}
	
	@Override
	public void createAllCourses(Questionnaire questionnaire) {
		
		for(CourseEnum courseName : CourseEnum.values()) {
			Course course = new Course(courseName, questionnaire);
			courseRepository.save(course);
		}		
	}
	
	@Override
	public double calculateCourseAvg(String courseName, Long questionnaireId) {
		
		try {
			return courseRepository.calculateCourseAvg(courseName, questionnaireId);
		} catch (AopInvocationException e) {
			return 0;
		}
	}
	
}
