package com.khal.intern_survey.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UnitRating implements Comparable<UnitRating> {
	
	private String name;
	private double rating;
	private int numberOfInterns;
	
	
	@Override
	public int compareTo(UnitRating o) {
		if(rating < o.getRating()) return 1;
		if(rating > o.getRating()) return -1;
		else return 0;
	}
	
}
