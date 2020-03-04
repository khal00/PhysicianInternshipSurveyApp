package com.khal.intern_survey.dto;

import javax.validation.constraints.NotBlank;

import com.khal.intern_survey.validation.FieldsValueMatch;
import com.khal.intern_survey.validation.ValidPassword;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@FieldsValueMatch.List({@FieldsValueMatch (field = "password", fieldMatch = "matchingPassword")})
public class PasswordDTO {
	
	@ValidPassword
	@NotBlank(message = "index.passwordisrequired")
	private String password;
	
	@NotBlank(message = "is required")
	private String matchingPassword;
	
}
