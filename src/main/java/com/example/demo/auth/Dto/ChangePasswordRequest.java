package com.example.demo.auth.Dto;

import com.example.demo.CustomeAnnotation.StrongPassword;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequest {


    private String currentPassword ;

    @StrongPassword
    private String newPassword ;

    @StrongPassword
    private String ConfirmationPassword ;




}
