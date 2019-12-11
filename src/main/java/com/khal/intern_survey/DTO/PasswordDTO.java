package com.khal.intern_survey.DTO;

import javax.validation.constraints.NotBlank;

import com.khal.intern_survey.validation.FieldsValueMatch;
import com.khal.intern_survey.validation.ValidPassword;

@FieldsValueMatch.List({@FieldsValueMatch (field = "password", fieldMatch = "matchingPassword")})
public class PasswordDTO {
	
	@ValidPassword
	@NotBlank(message = "index.passwordisrequired")
	private String password;
	
	@NotBlank(message = "is required")
	private String matchingPassword;

	public PasswordDTO() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	
}
