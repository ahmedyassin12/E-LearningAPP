package com.example.demo.CustomeAnnotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {


    String message() default " PhoneNumber must be 8 characters,   " +
            " with 8 numbers :";


    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}
