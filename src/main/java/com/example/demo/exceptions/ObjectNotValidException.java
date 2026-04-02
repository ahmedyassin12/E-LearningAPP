package com.example.demo.exceptions;



import lombok.AllArgsConstructor;

import java.util.Set;


public class ObjectNotValidException extends RuntimeException{




    private final Set<String> errors ;



    public ObjectNotValidException(Set<String> errors) {
        super(errors.toString()); // ← fixes getMessage() returning null
        this.errors = errors;
    }

    public Set<String> getErrors() {
        return errors;
    }





}
