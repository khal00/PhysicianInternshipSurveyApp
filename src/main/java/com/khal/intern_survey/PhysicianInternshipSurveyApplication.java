package com.khal.intern_survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.khal.intern_survey.service.UserService;

@SpringBootApplication
public class PhysicianInternshipSurveyApplication {
	
	@Autowired
	UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(PhysicianInternshipSurveyApplication.class, args);
		
	}

}
