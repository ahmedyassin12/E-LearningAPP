package com.example.demo.auth.Dto;

import com.example.demo.entity.Enums.Availability;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
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

    @JsonProperty("skillNames") // Tells Jackson: "Look exactly for this key"
    @NotEmpty(message = "At least one skill name must be provided")
    private Set<String> skillNames ;

    @NotEmpty (message = "Field name cannot be empty or null")
    private String fieldName;

    @Min(value = 0, message = "Experience time cannot be negative")
        private int experience_time ;

    @NotNull(message = "Please specify your availability status")
    private Availability availability ;



}
