package com.khal.intern_survey.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum MedicalChamberEnum {
	SZCZECIN("OIL w Szczecinie"), WARSZAWA("OIL w Warszawie"), POZNAN("WIL w Poznaniu");
	
	private final String name;

	
}
