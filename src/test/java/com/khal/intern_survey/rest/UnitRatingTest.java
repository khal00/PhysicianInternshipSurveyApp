package com.khal.intern_survey.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;

class UnitRatingTest {
	
	TestInfo testInfo;
	TestReporter testReporter;
	
	UnitRating unitRating1;
	UnitRating unitRating2;
	UnitRating unitRating3;
	UnitRating unitRating4;
	
	@BeforeEach
	void init(TestInfo testInfo, TestReporter testReporter) {
		this.testInfo = testInfo;
		this.testReporter = testReporter;
				
		testReporter.publishEntry("Running: " + testInfo.getDisplayName());
		
		unitRating1 = new UnitRating("Unit1", 5.2, 10);
		unitRating2 = new UnitRating("Unit2", 3.8, 10);
		unitRating3 = new UnitRating("Unit3", 2.0, 10);
		unitRating4 = new UnitRating("Unit4", 5.2, 10);
		
	}

//	@Test
//	@DisplayName("Testing compareTO method")
//	void testCompareTo() {
//		
//		assumeTrue(true);
//		
//		int expected = -1;
//		int actual = unitRating1.compareTo(unitRating2);
//		assertEquals(expected, actual, () -> "compare method should return -1 if  unit1 has higher rating than unit2");		
//	}
	
	@Test
	@DisplayName("Testing compareTo method with all cases")
	void testCompareToWithAssertAll() {
		
		assertAll(
				()-> assertEquals(-1, unitRating1.compareTo(unitRating2)),
				()-> assertEquals(1, unitRating2.compareTo(unitRating1)),
				()-> assertEquals(0, unitRating1.compareTo(unitRating4))
				);
	}

}
