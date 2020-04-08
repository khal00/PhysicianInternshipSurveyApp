package com.khal.intern_survey.enums;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class CourseEnumConverter implements AttributeConverter<CourseEnum, String> {
	
	@Override
	public String convertToDatabaseColumn(CourseEnum attribute) {
		if (attribute == null) {
			return null;
		}

		return attribute.getName();
	}

	@Override
	public CourseEnum convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		
		return Stream.of(CourseEnum.values())
				.filter(c -> c.getName().equals(dbData))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}
}
