package com.example.demo.Dtos.formationDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateFormationDto {



    @Valid
    @NotNull
    private FormationDto formationDto ;

    @NotNull
    private List<Long>  formateur_ids ;




}
