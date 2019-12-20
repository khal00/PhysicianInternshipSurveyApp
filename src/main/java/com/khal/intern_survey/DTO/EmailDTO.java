package com.khal.intern_survey.DTO;

import javax.validation.constraints.NotBlank;

import com.khal.intern_survey.validation.ValidEmail;

public class EmailDTO {
	
	@NotBlank(message = "index.emailrequired")
	@ValidEmail
	private String emailAddress;

	public EmailDTO() {
		super();
	}

	public EmailDTO(@NotBlank String emailAddress) {
		super();
		this.emailAddress = emailAddress;
	}


	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

}
