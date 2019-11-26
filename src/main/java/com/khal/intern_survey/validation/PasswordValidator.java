package com.khal.intern_survey.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String>{
	
	private Pattern pattern;
	private Matcher matcher;
	private static final String PASSWORD_PATTERN = "[0-9]+";

	@Override
	public boolean isValid(final String password, final ConstraintValidatorContext context) {
		
		if(password == null) {
			return true;
		}
		pattern = Pattern.compile(PASSWORD_PATTERN);
		matcher = pattern.matcher(password);
		return matcher.matches();
	}

}
