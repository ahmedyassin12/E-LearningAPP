package com.example.demo.Dtos.formationDto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class FormationManagerDto extends FormationDto {

    private String publicId ;


}
