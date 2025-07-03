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
public class CreateFormateurDto extends FormateurDto{


    @StrongPassword
    private String password ;




}
