package com.khal.intern_survey.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.khal.intern_survey.dao.QuestionnaireRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class QuestionnaireServiceImplTest {
	
	
	@Autowired
	QuestionnaireService qs;
	
	@MockBean
	private QuestionnaireRepository qr;
	
	@Test
    public void testFindById() {
		
//		when(qr.findById(any(Long.class))).thenReturn(value)
	}

	
	

}
