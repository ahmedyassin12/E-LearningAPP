package com.example.demo.Dtos.userDto;

import com.example.demo.CustomeAnnotation.PhoneNumber;
import com.example.demo.CustomeAnnotation.UsernameValidator;
import com.example.demo.entity.Enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @Email
    private String email;

    @UsernameValidator
    private String username;

    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;

    @PhoneNumber
    private String phoneNumber;

    @NotNull
    private LocalDate dateNaissance;

    @NotNull
    private Role role;


}
