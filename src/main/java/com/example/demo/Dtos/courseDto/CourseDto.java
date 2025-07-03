package com.example.demo.Dtos.courseDto;

import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class CourseDto {


    @Valid
    private InitiateCourseDto initiateCourseDto ;

    private String coursevideo_url;

    private String coursePdf_url;






}
