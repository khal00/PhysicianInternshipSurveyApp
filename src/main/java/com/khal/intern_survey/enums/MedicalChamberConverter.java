package com.khal.intern_survey.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import java.util.stream.*;

@Converter(autoApply = true)
public class MedicalChamberConverter implements AttributeConverter<MedicalChamberEnum, String> {

	@Override
	public String convertToDatabaseColumn(MedicalChamberEnum attribute) {
		if (attribute == null) {
			return null;
		}

		return attribute.getName();
	}

	@Override
	public MedicalChamberEnum convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		
		return Stream.of(MedicalChamberEnum.values())
				.filter(c -> c.getName().equals(dbData))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
