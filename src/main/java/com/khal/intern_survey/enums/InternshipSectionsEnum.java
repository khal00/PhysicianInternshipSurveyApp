package com.khal.intern_survey.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum InternshipSectionsEnum {
	INTERNAL_MEDICINE("Internal Medicine"), PEDIATRICS("Pediatrics"), SURGERY("General Surgery")
	, OBSTERICS_GYNECOLOGY("Obsterics and Gynecology"), ANESTHESIOLOGY_INTENSIVE_CARE("Anesthesiology and Intensive Care")
	, PSYCHIATRY("Psychiatry"), FAMILY_MEDICINE("Family Medicine");
	
	private String name;
}	
