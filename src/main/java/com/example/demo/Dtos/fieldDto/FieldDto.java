package com.example.demo.Dtos.fieldDto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldDto {


    @NotEmpty
    @UniqueElements
    private String fieldName;

    @NotEmpty
    private String description;






}
