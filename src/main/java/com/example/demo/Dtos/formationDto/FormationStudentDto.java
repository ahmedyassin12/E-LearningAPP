package com.example.demo.Dtos.formationDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class FormationStudentDto extends FormationDto {



    boolean IsEnrollementpaid ;

    boolean isStudentEnrolled;




}
