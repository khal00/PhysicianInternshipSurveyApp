package com.khal.intern_survey.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.khal.intern_survey.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
	
	@Query(value = "select avg(NULLIF(rating,0)) as course_avg from \r\n" + 
			"(select tutor as rating from course where name= :courseName and questionnaire_id= :questionnaireId \r\n" + 
			"union all select unit from course where name= :courseName and questionnaire_id= :questionnaireId \r\n" + 
			"union all select theoretical_knowledge from course where name= :courseName and questionnaire_id= :questionnaireId \r\n" + 
			"union all select practical_knowledge from course where name= :courseName and questionnaire_id= :questionnaireId \r\n" +  
			") course_avg;"
			, nativeQuery = true)
	public double calculateCourseAvg(@Param("courseName") String courseName, @Param("questionnaireId") Long questionnaireId);

}
