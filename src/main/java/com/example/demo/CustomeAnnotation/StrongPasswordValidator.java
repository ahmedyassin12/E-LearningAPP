package com.example.demo.CustomeAnnotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class  StrongPasswordValidator
        implements ConstraintValidator<StrongPassword, String> {

    private static final String SPECIAL_CHARS = "!@#$%^&*()-_+=<>?/[]{}";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {

        if (password == null || password.length() < 8) return false;

        boolean hasUpper = false, hasLower = false,
                hasDigit = false, hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (SPECIAL_CHARS.indexOf(c) != -1) hasSpecial = true;

            // Early exit if all conditions are met
            if (hasUpper && hasLower && hasDigit && hasSpecial) return true;


        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }



}


