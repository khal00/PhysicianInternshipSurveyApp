package com.khal.intern_survey.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.khal.intern_survey.entity.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
