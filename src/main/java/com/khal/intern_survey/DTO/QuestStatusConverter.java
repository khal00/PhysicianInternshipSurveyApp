package com.khal.intern_survey.DTO;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;

import com.khal.intern_survey.entity.Questionnaire.Status;

public class QuestStatusConverter implements AttributeConverter<Status, String> {

	@Override
	public String convertToDatabaseColumn(Status attribute) {
		if (attribute == null) {
			return null;
		}

		return attribute.getName();
	}

	@Override
	public Status convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		
		return Stream.of(Status.values())
				.filter(c -> c.getName().equals(dbData))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
