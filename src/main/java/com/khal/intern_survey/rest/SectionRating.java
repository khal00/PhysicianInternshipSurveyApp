package com.khal.intern_survey.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class SectionRating {
	
	private String name;
	private String unit;
	private double rating;
	private int numberOfInterns;
	
}
