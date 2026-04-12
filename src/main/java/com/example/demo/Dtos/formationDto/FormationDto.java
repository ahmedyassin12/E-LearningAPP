package com.example.demo.Dtos.formationDto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class FormationDto {


    private Long formation_id;



    @NotEmpty
    private String  formation_name ;

    private String imageUrl ;


    @NotEmpty
    private String description;

    @NotNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate date;











}
