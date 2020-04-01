package com.khal.intern_survey;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class PhysicianInternshipSurveyApplicationTest {
	
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void apiControllerWithUnitVarShouldReturnUnitRating() throws Exception {
		this.mockMvc.perform(get("/api/rating/unit/Samodzielny Publiczny Szpital Kliniczny Nr 1 w Szczecinie"))
		.andExpect(status().isOk()).andExpect(content()
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.name").value("Samodzielny Publiczny Szpital Kliniczny Nr 1 w Szczecinie"));
	}
	


}
