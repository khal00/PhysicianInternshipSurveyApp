package com.khal.intern_survey.validation;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.DigitCharacterRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.UppercaseCharacterRule;
import org.passay.WhitespaceRule;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

	@Override
	public void initialize(ValidPassword arg0) {
	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {

		if (password.equalsIgnoreCase("")) {
			return true;
		}

		PasswordValidator validator = new PasswordValidator(Arrays.asList(new LengthRule(8, 30),
				new UppercaseCharacterRule(1), new DigitCharacterRule(1), new WhitespaceRule()));
//			new SpecialCharacterRule(1),
//          new NumericalSequenceRule(3,false), 
//          new AlphabeticalSequenceRule(3,false), 
//          new QwertySequenceRule(3,false),

		RuleResult result = validator.validate(new PasswordData(password));
		if (result.isValid()) {
			return true;
		}

		return false;
	}

}
