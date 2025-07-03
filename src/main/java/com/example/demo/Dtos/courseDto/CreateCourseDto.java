package com.example.demo.Dtos.courseDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateCourseDto {

    @Valid
    @NotNull
    private InitiateCourseDto initiateCourseDto ;

    @NotNull
    private Long formation_id ;

    private String publicId ;

}
