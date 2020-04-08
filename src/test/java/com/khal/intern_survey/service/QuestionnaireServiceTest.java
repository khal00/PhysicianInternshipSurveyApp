package com.khal.intern_survey.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.khal.intern_survey.dao.QuestionnaireRepository;
import com.khal.intern_survey.entity.Course;
import com.khal.intern_survey.entity.InternshipSection;
import com.khal.intern_survey.entity.Questionnaire;
import com.khal.intern_survey.enums.CourseEnum;
import com.khal.intern_survey.enums.InternshipSectionsEnum;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class QuestionnaireServiceTest {
	
	private Questionnaire quest1;
	private Questionnaire quest2;
	private Questionnaire quest3;
	private List<Questionnaire> questionnaires;
	
	@Mock
	private QuestionnaireRepository qRepository;
	
	@Mock
	private InternshipSectionService sService;
	
	@Mock
	private CourseService cService;
	
	@InjectMocks
	QuestionnaireServiceImpl qService;
	
	@BeforeEach
	void createNewQuestionnaire() {
		quest1 = new Questionnaire(1L, null, LocalDateTime.now());
		quest1.setCoordinator(5);
		quest2 = new Questionnaire(2L, null, LocalDateTime.now());
		quest2.setCoordinator(4);
		quest3 = new Questionnaire(3L, null, LocalDateTime.now());
		quest3.setCoordinator(2);
		createAndSetAllSections(quest1);
		createAndSetAllCourses(quest1);
		createAndSetAllSections(quest2);
		createAndSetAllCourses(quest2);
		createAndSetAllSections(quest3);
		createAndSetAllCourses(quest3);
		questionnaires = Arrays.asList(quest1, quest2, quest3);
		
	}
	
	@Test
	void injectedComponentsAreNotNull() {
		assertThat(qService).isNotNull();
		assertThat(qRepository).isNotNull();
		assertNotNull(sService);
	}
	
	@Test
    void findByIdShouldNotBeNull() {
		when(qRepository.findById(any(Long.class))).thenReturn(Optional.of(quest1));
		Questionnaire tempQuestionnaire = qService.findById(1L);
		assertNotNull(tempQuestionnaire);
	}
	
	@Test
	void calculateQuestionnaireAvgWhenAllSectionsNotDisabledShouldReturnRightValue() {
		when(sService.calculateSectionAvg(eq("Internal Medicine"), eq(1L))).thenReturn(3.66);
		when(sService.calculateSectionAvg(eq("Pediatrics"), eq(1L))).thenReturn(4.0);
		when(sService.calculateSectionAvg(eq("General Surgery"), eq(1L))).thenReturn(5.33);
		when(sService.calculateSectionAvg(eq("Obsterics and Gynecology"), eq(1L))).thenReturn(1.0);
		when(sService.calculateSectionAvg(eq("Anesthesiology and Intensive Care"), eq(1L))).thenReturn(2.5);
		when(sService.calculateSectionAvg(eq("Psychiatry"), eq(1L))).thenReturn(6.1);
		when(sService.calculateSectionAvg(eq("Family Medicine"), eq(1L))).thenReturn(4.0);
		double actual = qService.calculateQuestionnaireAvg(quest1);
		double expected = ((3.66 + 4.0 + 5.33 + 1.0 + 2.5 + 6.1 + 4.0) + quest1.getCoordinator()) / 8;
		assertEquals(expected, actual);
	}
	
	@Test
	void calculateQuestionnaireAvgWhenOneSectionsIsDisabledShouldReturnRightValue() {
		quest1.getSections().get(1).setDisabled(true);
		when(sService.calculateSectionAvg(anyString(), anyLong())).thenReturn(4.0);
		double actual = qService.calculateQuestionnaireAvg(quest1);
		double expected = ((6 * 4.0) + quest1.getCoordinator()) / 7;
		assertEquals(expected, actual);
	}
	
	@Test
	void calculateQuestionnaireAvgWhenAllSectionsAreDisabledShouldReturnRightValue() {
		for(InternshipSection is : quest1.getSections()) {
			is.setDisabled(true);
		}
		double actual = qService.calculateQuestionnaireAvg(quest1);
		double expected = quest1.getCoordinator();
		assertEquals(expected, actual);
	}
	
	@Test
	void calculateQuestionnairesListAvgShuldReturnRightValue() {
		when(sService.calculateSectionAvg(anyString(), eq(1L))).thenReturn(4.0);
		when(sService.calculateSectionAvg(anyString(), eq(2L))).thenReturn(3.0);
		when(sService.calculateSectionAvg(anyString(), eq(3L))).thenReturn(6.0);
		double actual = qService.calculateQuestionnairesAvg(questionnaires);
		double q1avg = ((7 * 4.0) + quest1.getCoordinator()) / 8;
		double q2avg = ((7 * 3.0) + quest2.getCoordinator()) / 8;
		double q3avg = ((7 * 6.0) + quest3.getCoordinator()) / 8;
		System.out.println(q1avg);
		System.out.println(q2avg);
		System.out.println(q3avg);
		double expected = (q1avg + q2avg + q3avg) / 3;
		assertEquals(expected, actual);
	}
	
	@Test
	void calculateCourseAvgFromQuestionnairesListShouldReturnRightValue() {
		when(cService.calculateCourseAvg(anyString(), eq(1L))).thenReturn(3.25);
		when(cService.calculateCourseAvg(anyString(), eq(2L))).thenReturn(4.5);
		when(cService.calculateCourseAvg(anyString(), eq(3L))).thenReturn(5.0);
		double actual = qService.calculateCourseAvg(CourseEnum.TRANSFUSIOLOGY, questionnaires);
		double expected = (3.25 + 4.50 + 5.0) / 3;
		assertEquals(expected, actual);
	}
	
	@Test
	void calculateCourseAvgFromQuestionnairesListWhenOneCourseIsDisabledShouldReturnRightValue() {
		when(cService.calculateCourseAvg(anyString(), eq(1L))).thenReturn(3.25);
		when(cService.calculateCourseAvg(anyString(), eq(2L))).thenReturn(4.5);
		when(cService.calculateCourseAvg(anyString(), eq(3L))).thenReturn(0.0);
		double actual = qService.calculateCourseAvg(CourseEnum.TRANSFUSIOLOGY, questionnaires);
		double expected = (3.25 + 4.50) / 2;
		assertEquals(expected, actual);
	}
	
	
	@Test
	void getSectionNumberOfInternsShouldReturnRightValue() {
		when(sService.calculateSectionAvg(anyString(), eq(1L))).thenReturn(4.0);
		when(sService.calculateSectionAvg(anyString(), eq(2L))).thenReturn(3.0);
		when(sService.calculateSectionAvg(anyString(), eq(3L))).thenReturn(0.0);
		int actual = qService.getSectionNumberOfInterns(InternshipSectionsEnum.PEDIATRICS, questionnaires);
		int expected = 2;
		assertEquals(expected, actual);
	}
	
	@Test
	void getCourseNumberOfInternsShouldReturnZero() {
		when(cService.calculateCourseAvg(anyString(), eq(1L))).thenReturn(0.0);
		when(cService.calculateCourseAvg(anyString(), eq(2L))).thenReturn(0.0);
		when(cService.calculateCourseAvg(anyString(), eq(3L))).thenReturn(0.0);
		int actual = qService.getCourseNumberOfInterns(CourseEnum.BIOETHICS, questionnaires);
		int expected = 0;
		assertEquals(expected, actual);
	}

	
	private void createAndSetAllSections(Questionnaire quest){
		List<InternshipSection> sections = new ArrayList<>();
		for(InternshipSectionsEnum sectionName : InternshipSectionsEnum.values()) {
			InternshipSection section = new InternshipSection(sectionName);
			sections.add(section);
		}
		quest.setSections(sections);
	}
	
	private void createAndSetAllCourses(Questionnaire quest){
		List<Course> courses = new ArrayList<>();
		for(CourseEnum courseName : CourseEnum.values()) {
			Course c = new Course(courseName);
			courses.add(c);
		}
		quest.setCourses(courses);
	}
	

	
	

}
