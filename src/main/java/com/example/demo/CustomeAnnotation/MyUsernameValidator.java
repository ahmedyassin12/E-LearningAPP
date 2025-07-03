package com.example.demo.CustomeAnnotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MyUsernameValidator implements ConstraintValidator<UsernameValidator,String> {




    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {




        return  s != null && s.matches("^[a-zA-Z0-9]{4,}$");



    }







}
