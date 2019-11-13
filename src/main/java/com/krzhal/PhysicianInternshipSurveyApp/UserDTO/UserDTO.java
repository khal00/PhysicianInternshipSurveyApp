package com.krzhal.PhysicianInternshipSurveyApp.UserDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserDTO {
	
	@NotBlank(message = "is required")
	private String username;
	
	@NotBlank(message = "is required")
	private String password;
	
	@NotBlank(message = "is required")
	private String matchingPassword;

	public UserDTO() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
