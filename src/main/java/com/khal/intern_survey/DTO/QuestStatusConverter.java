package com.khal.intern_survey.DTO;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.khal.intern_survey.entity.Questionnaire.Status;

//@Converter(autoApply = true)
public class QuestStatusConverter implements AttributeConverter<Status, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Status attribute) {
		if (attribute == null) {
			return null;
		}

		return attribute.ordinal();
	}

	@Override
	public Status convertToEntityAttribute(Integer dbData) {
		if (dbData == null) {
			return null;
		}
		
		return Stream.of(Status.values())
				.filter(s -> s.ordinal() == dbData)
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
