package com.example.uservalidation.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AccountInstagramValidator implements ConstraintValidator<AccountInstagramValidation, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.isEmpty() || s.startsWith("@");
    }
}
