package com.khal.intern_survey.DTO;

import javax.validation.constraints.NotBlank;
import com.khal.intern_survey.validation.ValidEmail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EmailDTO {
	
	@NotBlank(message = "index.emailrequired")
	@ValidEmail
	private String emailAddress;

}
