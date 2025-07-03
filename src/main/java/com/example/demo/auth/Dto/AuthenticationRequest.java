package com.example.demo.auth.Dto;

import com.example.demo.CustomeAnnotation.StrongPassword;
import com.example.demo.CustomeAnnotation.UsernameValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {


    @UsernameValidator
    private String username ;

    @StrongPassword
    private String password ;


}
