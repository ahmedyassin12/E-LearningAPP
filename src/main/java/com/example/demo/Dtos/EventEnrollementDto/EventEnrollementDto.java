package com.example.demo.Dtos.EventEnrollementDto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventEnrollementDto {

    private Long id;


    private Integer rating;

    @NotNull
    private LocalDate enrollementDate;


    @NotEmpty
    private String eventName;

    @NotNull
    private Long studentID;




}

