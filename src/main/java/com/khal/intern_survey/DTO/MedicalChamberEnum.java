package com.khal.intern_survey.DTO;

public enum MedicalChamberEnum {
	SZCZECIN("OIL w Szczecinie"), WARSZAWA("OIL w Warszawie"), POZNAN("WIL w Poznaniu");
	
	private String name;

	private MedicalChamberEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
