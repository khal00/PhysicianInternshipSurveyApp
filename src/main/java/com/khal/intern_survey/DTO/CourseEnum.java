package com.khal.intern_survey.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum CourseEnum {
	
	TRANSFUSIOLOGY("Clinical Transfusiology"), HIV("Prevention of HIV infection"), EMERGENCY("Emergency Medicine")
	, CERTIFICATION("Medical Certification"), BIOETHICS("Bioethics"), LAW("Medical Law");
	
	private String name;
	
}
