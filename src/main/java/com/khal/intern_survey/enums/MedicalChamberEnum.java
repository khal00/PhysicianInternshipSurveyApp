package com.khal.intern_survey.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum MedicalChamberEnum {
	WROCŁAW("Dolnośląska Izba Lekarska we Wrocławiu"), SZCZECIN("Okręgowa Izba Lekarska w Szczecinie")
	, WARSZAWA("Okręgowa Izba Lekarska w Warszawie"), POZNAŃ("Wielkopolska Izba Lekarska w Poznaniu")
	, ZIELONA("Okręgowa Izba Lekarska w Zielonej Górze"), RZESZÓW("Okręgowa Izba Lekarska w Rzeszowie");
	
	private final String name;

	
}
