package com.example.demo.Dtos.userDto;

import com.example.demo.CustomeAnnotation.StrongPassword;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentDto extends StudentDto {


    @StrongPassword
    private String password ;

    private String publicId ;






}
