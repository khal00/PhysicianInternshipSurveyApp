package com.khal.intern_survey.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.enums.MedicalChamberEnum;
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
	
	
	@Test
	void showCourseRatingByChamberShouldContainsAppropriateContent() throws Exception {
		List<Questionnaire> questionnaires = new ArrayList<>();
		when(questionnaireService.findAllAcceptedQuestionnairesByMedicalChamber(any())).thenReturn(questionnaires);
		when(questionnaireService.calculateCourseAvg(any(), eq(questionnaires))).thenReturn(5.0);
		when(questionnaireService.getCourseNumberOfInterns(any(), eq(questionnaires))).thenReturn(25);
		mockMvc.perform(get("/api/rating/course/BIOETHICS/chamber/SZCZECIN"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.name").value("BIOETHICS"));
	}

}
