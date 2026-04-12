package com.example.demo.mapper;

import com.example.demo.Dtos.fieldDto.FieldDto;
import com.example.demo.Dtos.skillDto.SkillDto;
import com.example.demo.entity.Field;
import com.example.demo.entity.Skill;

public class FieldMapper {



    public FieldDto returnFieldDto(Field field){


    return FieldDto.builder()
            .fieldName(field.getFieldName())
            .description(field.getDescription())
            .build();
    }

    public Field returnField(FieldDto fieldDto ){

        return Field .builder()
                .fieldName(fieldDto.getFieldName())
                .description(fieldDto.getDescription())
                .build();

    }


}
