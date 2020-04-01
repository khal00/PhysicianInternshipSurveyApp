package com.khal.intern_survey.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.khal.intern_survey.dto.MedicalChamberEnum;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.rest.CourseRating;
import com.khal.intern_survey.rest.SectionRating;
import com.khal.intern_survey.rest.UnitRating;
import com.khal.intern_survey.service.InternshipSectionService;
import com.khal.intern_survey.service.InternshipUnitService;
import com.khal.intern_survey.service.QuestionnaireService;
import com.khal.intern_survey.service.UserService;

@WebMvcTest(ApiController.class)
class ApiControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private QuestionnaireService questionnaireService;
	
	@MockBean
	private InternshipUnitService unitService;
	
	@MockBean
	private CourseRating courseRating;
	
	@MockBean
	private UnitRating unitRating;
	
	@MockBean
	private SectionRating sectionRating;
	
	@Test
	void test() throws Exception {
		
	}

}
