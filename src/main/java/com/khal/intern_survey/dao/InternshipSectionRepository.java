package com.khal.intern_survey.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.khal.intern_survey.entity.InternshipSection;

public interface InternshipSectionRepository extends JpaRepository<InternshipSection, Long> {
	
	@Query(value = "select avg(NULLIF(rating,0)) as section_avg from \r\n" + 
			"(select tutor as rating from internship_section where name= :sectionName and questionnaire_id= :questionnaireId \r\n" + 
			"union all select unit from internship_section where name= :sectionName and questionnaire_id= :questionnaireId \r\n" + 
			"union all select number_of_procedures from internship_section where name= :sectionName and questionnaire_id= :questionnaireId \r\n" + 
			"union all select procedures_autonomy from internship_section where name= :sectionName and questionnaire_id= :questionnaireId \r\n" + 
			"union all select theoretical_knowledge from internship_section where name= :sectionName and questionnaire_id= :questionnaireId \r\n" + 
			"union all select practical_knowledge from internship_section where name= :sectionName and questionnaire_id= :questionnaireId \r\n" + 
			"union all select medical_duty from internship_section where name= :sectionName and questionnaire_id= :questionnaireId \r\n" + 
			"union all select ward from internship_section where name= :sectionName and questionnaire_id= :questionnaireId \r\n" + 
			"union all select clinic from internship_section where name= :sectionName and questionnaire_id= :questionnaireId \r\n" + 
			") section_avg;"
			, nativeQuery = true)
	public double calculateSectionAvg(@Param("sectionName") String sectionName, @Param("questionnaireId") Long questionnaireId);

}
