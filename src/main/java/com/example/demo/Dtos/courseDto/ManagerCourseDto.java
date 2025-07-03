package com.example.demo.Dtos.courseDto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ManagerCourseDto extends CourseDto {



    private String publicId ;



}
