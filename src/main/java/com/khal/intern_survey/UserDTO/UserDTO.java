package com.khal.intern_survey.UserDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDTO {
	
	@NotBlank(message = "is required")
	private String email;
	
	@NotBlank(message = "is required")
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
