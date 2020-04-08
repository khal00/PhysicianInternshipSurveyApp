package com.khal.intern_survey.rest;

import com.khal.intern_survey.enums.CourseEnum;
import com.khal.intern_survey.enums.MedicalChamberEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CourseRating {
	
	private CourseEnum name;
	private MedicalChamberEnum medicalChamber;
	private double rating;
	private int numberOfInterns;

}
