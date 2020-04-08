package com.khal.intern_survey.enums;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class InternshipSectionsEnumConverter implements AttributeConverter<InternshipSectionsEnum, String> {
	
	@Override
	public String convertToDatabaseColumn(InternshipSectionsEnum attribute) {
		if (attribute == null) {
			return null;
		}

		return attribute.getName();
	}

	@Override
	public InternshipSectionsEnum convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		
		return Stream.of(InternshipSectionsEnum.values())
				.filter(c -> c.getName().equals(dbData))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}
}
