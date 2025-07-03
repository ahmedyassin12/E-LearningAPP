package com.example.demo.Dtos.userDto;


import com.example.demo.entity.Enums.Availability;
import com.example.demo.entity.Skill;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FormateurDto extends UserDto {


    @NotEmpty
    private Set<String> SkillNames ;

    @NotEmpty
    private String fieldName;

    @NotNull
    private Integer experience_time ;

    @NotNull
    private Availability availability ;



}
