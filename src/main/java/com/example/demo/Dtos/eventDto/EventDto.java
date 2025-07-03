package com.example.demo.Dtos.eventDto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EventDto {


    private Long eventId;

    @NotEmpty
    private String EventName ;

    @NotNull
    private LocalDate date;

    @NotEmpty
    private String description;

    @NotEmpty
    private String cityLocation;

    private String imageUrl;


    @NotEmpty
    private String streetLocation ;





}

