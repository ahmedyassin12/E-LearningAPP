package com.example.demo.auth.Dto;

import com.example.demo.entity.Enums.Availability;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FormateurRequest extends RegisterRequest{


    @NotEmpty
    private Set<String> SkillNames ;

    @NotEmpty
    private String fieldName;

    @NotNull
    private int experience_time ;

    @NotNull
    private Availability availability ;



}
