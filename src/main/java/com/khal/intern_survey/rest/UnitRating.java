package com.khal.intern_survey.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class UnitRating {
	
	private final String name;
	
	private final double rating;
	
	private final int numberOfInterns;
	
}
