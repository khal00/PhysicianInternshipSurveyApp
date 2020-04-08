package com.khal.intern_survey.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum MedicalChamberEnum {
	WROCŁAW("DIL we Wrocławiu"), SZCZECIN("OIL w Szczecinie"), WARSZAWA("OIL w Warszawie"), POZNAŃ("WIL w Poznaniu");
	
	private final String name;

	
}
