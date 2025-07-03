package com.example.demo.validator;

import com.example.demo.exceptions.ObjectNotValidException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ObjectValidator<T> {


    private final ValidatorFactory factory= Validation.buildDefaultValidatorFactory();

    private final Validator validator= factory.getValidator() ;



    public void validate( T objectToValidate ){


        Set< ConstraintViolation<T> > violations=validator.validate(objectToValidate) ;

        if(!violations.isEmpty()){

          Set<String> message=  violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet()) ;

                           throw new ObjectNotValidException(message) ;

        }







    }












}
