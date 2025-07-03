package com.example.demo.Dtos.courseDto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InitiateCourseDto {


        
        private long course_id;

        @NotEmpty
        private String courseName;

        @NotEmpty
        private String course_description;




}
