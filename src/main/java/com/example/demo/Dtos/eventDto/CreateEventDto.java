package com.example.demo.Dtos.eventDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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
public class CreateEventDto {


    @Valid
    @NotNull
    private EventDto eventDto  ;

    @NotEmpty
    private List<Long> formateur_ids ;


}
