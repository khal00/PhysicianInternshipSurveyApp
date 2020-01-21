package com.khal.intern_survey.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum MedicalChamberEnum {
	WROCŁAW("DIL we Wrocławiu"), SZCZECIN("OIL w Szczecinie"), WARSZAWA("OIL w Warszawie"), POZNAN("WIL w Poznaniu");
	
	private final String name;

	
}
