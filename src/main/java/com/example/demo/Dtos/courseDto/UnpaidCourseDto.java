package com.example.demo.Dtos.courseDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UnpaidCourseDto {


  @Valid
  @NotNull
  private InitiateCourseDto initiateCourseDto ;



}
