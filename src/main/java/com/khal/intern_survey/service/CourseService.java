package com.khal.intern_survey.service;

import com.khal.intern_survey.entity.Course;
import com.khal.intern_survey.entity.Questionnaire;

public interface CourseService {
	
	public void saveCourse(Course course);

	public void createAllCourses(Questionnaire questionnaire);

}
