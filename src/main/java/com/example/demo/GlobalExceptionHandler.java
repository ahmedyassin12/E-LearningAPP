package com.example.demo;
import com.example.demo.exceptions.ObjectNotValidException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {




    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?>handleException(IllegalStateException e){

return ResponseEntity
        .badRequest()
        .body(e.getMessage());


    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?>handleException(EntityNotFoundException e){

        return ResponseEntity
                .notFound()
                .build() ;


    }
    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<?>handleException(UnexpectedTypeException e){

        return ResponseEntity
                .badRequest()
                .body(e.getMessage()) ;


    }

    @ExceptionHandler(ObjectNotValidException.class)
    public ResponseEntity<?>handleException(ObjectNotValidException e){

        return ResponseEntity
                .badRequest()
                .body(e.getMessage());


    }


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?>handleException(BadCredentialsException e){

        return ResponseEntity
                .badRequest()
                .body(e.getMessage()) ;


    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?>handleException(IllegalArgumentException e){

        return ResponseEntity
                .badRequest()
                .body(e.getMessage()) ;


    }
    @ExceptionHandler(ServletException.class)
    public ResponseEntity<?>handleException(ServletException e){

        return ResponseEntity
                .badRequest()
                .body(e.getMessage()) ;


    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity<?>handleException(IOException e){

        return ResponseEntity
                .badRequest()
                .body(e.getMessage()) ;


    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?>handleException(UsernameNotFoundException e){

        return ResponseEntity
                .badRequest()
                .body(e.getMessage()) ;


    }
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?>handleException(DisabledException e){

        return ResponseEntity
                .badRequest()
                .body(e.getMessage()) ;


    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?>handleException(NullPointerException e){

        return ResponseEntity
                .badRequest()
                .body(e.getMessage()) ;


    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?>handleException(NoSuchElementException e){

        return ResponseEntity
                .badRequest()
                .body(e.getMessage()) ;


    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?>handleException(RuntimeException e){

        return ResponseEntity
                .badRequest()
                .body(e.getMessage()) ;


    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?>handleException(ExpiredJwtException e){

        return ResponseEntity
                .badRequest()
                .body(e.getMessage()) ;


    }



}
