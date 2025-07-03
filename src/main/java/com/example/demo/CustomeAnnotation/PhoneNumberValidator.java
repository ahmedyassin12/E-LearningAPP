package com.example.demo.CustomeAnnotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber,String> {




    @Override
    public boolean isValid(String p, ConstraintValidatorContext constraintValidatorContext) {
        if (p == null || !p.matches("\\d{8}")) {
            return false; // must be exactly 8 digits
        }

        char firstDigit = p.charAt(0);

        return firstDigit == '2' || firstDigit == '4' || firstDigit == '5' ||
                firstDigit == '7' || firstDigit == '9';

    }





}
