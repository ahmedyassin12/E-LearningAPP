package com.example.demo.Dtos.userDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto extends UserDto {




    private String imageUrl ;



    @NotNull
    @Min(10)
    private Integer age ;












}
