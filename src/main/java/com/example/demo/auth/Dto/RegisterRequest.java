package com.example.demo.auth.Dto;
import com.example.demo.CustomeAnnotation.PhoneNumber;
import com.example.demo.CustomeAnnotation.StrongPassword;
import com.example.demo.CustomeAnnotation.UsernameValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {



    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;

    @Email
    private String email;
    @UsernameValidator
    private String username;

    @StrongPassword
    private String password ;

    @PhoneNumber
    private String phoneNumber;


    private LocalDate dateNaissance;



 }
