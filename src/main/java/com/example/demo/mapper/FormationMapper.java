package com.example.demo.mapper;

import com.example.demo.Dtos.formationDto.CreateFormationDto;
import com.example.demo.Dtos.formationDto.FormationDto;
import com.example.demo.Dtos.formationDto.FormationManagerDto;
import com.example.demo.Dtos.formationDto.FormationStudentDto;
import com.example.demo.entity.Formateur;
import com.example.demo.entity.Formation;

import java.util.Set;

public class FormationMapper {


    public FormationDto returnformationDto(Formation formation){


        return FormationStudentDto.builder()
                .formation_id(formation.getId())
                .description(formation.getDescription())
                .date(formation.getDate())
                .formation_name(formation.getFormationName())
                .imageUrl(formation.getImageUrl())
                .build();

    }


    public FormationStudentDto returnformationStudentDto(Formation formation, boolean isPaid, boolean isEnrolled){


        return FormationStudentDto.builder()
                .formation_id(formation.getId())
                .description(formation.getDescription())
                .date(formation.getDate())
                .formation_name(formation.getFormationName())
                .imageUrl(formation.getImageUrl())
                .IsEnrollementpaid(isPaid)
                .isStudentEnrolled(isEnrolled)
                .build();

    }



    public Formation ToFormationForCreation(CreateFormationDto createFormationDto,
                                         Set<Formateur> formateurs){

        return Formation.builder()
                .id(createFormationDto.getFormationDto().getFormation_id())
                .description(createFormationDto.getFormationDto().getDescription())
                .date(createFormationDto.getFormationDto().getDate())
                .formationName(createFormationDto.getFormationDto().getFormation_name())
                .formateurs(formateurs)
                .build();



    }


    public Formation returnFormation(CreateFormationDto createFormationDto , Formation formation)

    {


        formation.setFormationName(createFormationDto.getFormationDto().getFormation_name());
        formation.setDescription(createFormationDto.getFormationDto().getDescription());
        formation.setDate(createFormationDto.getFormationDto().getDate());
        formation.setId(createFormationDto.getFormationDto().getFormation_id());
        return formation ;

    }




    public FormationManagerDto returnformationManagerDto(Formation formation){


        return FormationManagerDto.builder()
                .formation_id(formation.getId())
                .description(formation.getDescription())
                .date(formation.getDate())
                .formation_name(formation.getFormationName())
                .imageUrl(formation.getImageUrl())
                .publicId(formation.getPublicId())
                .build();

    }





}
