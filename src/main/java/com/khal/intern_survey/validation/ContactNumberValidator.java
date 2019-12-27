package com.khal.intern_survey.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactNumberValidator implements
ConstraintValidator<ContactNumberConstraint, String> {

  @Override
  public void initialize(ContactNumberConstraint contactNumber) {
  }

  @Override
  public boolean isValid(String contactField,
    ConstraintValidatorContext cxt) {
	  if(contactField == null) {
		  return true;
	  }
	  
      return contactField != null && contactField.matches("[0-9]+")
        && (contactField.length() > 8) && (contactField.length() < 14);
  }

}
