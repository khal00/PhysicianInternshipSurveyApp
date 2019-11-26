package com.khal.intern_survey.UserDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.khal.intern_survey.validation.FieldsValueMatch;
import com.khal.intern_survey.validation.ValidEmail;
import com.khal.intern_survey.validation.ValidPassword;


@FieldsValueMatch.List({@FieldsValueMatch (field = "password", fieldMatch = "matchingPassword")})
public class UserDTO {
	
	@ValidEmail
	@NotBlank(message = "index.emailrequired")
	private String email;
	
	@ValidPassword
	@NotBlank(message = "index.passwordisrequired")
	private String password;
	
	@NotBlank(message = "is required")
	private String matchingPassword;

	public UserDTO() {
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
