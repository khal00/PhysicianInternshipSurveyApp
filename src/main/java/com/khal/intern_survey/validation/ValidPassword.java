package com.khal.intern_survey.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = PasswordConstraintValidator.class)
@Retention(RUNTIME)
@Target({ FIELD })
@Documented
public @interface ValidPassword {
	
	String message() default "index.invalidpassword";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
