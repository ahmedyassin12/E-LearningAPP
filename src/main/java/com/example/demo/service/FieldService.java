package com.example.demo.service;

import com.example.demo.Dtos.fieldDto.FieldDto;
import com.example.demo.dao.FieldDao;

import com.example.demo.entity.Field;

import com.example.demo.entity.Skill;
import com.example.demo.mapper.FieldMapper;
import com.example.demo.validator.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FieldService {

    private final FieldDao fieldRepository;
    private final ObjectValidator<FieldDto> fieldValidator;

    private FieldMapper fieldMapper=new FieldMapper();
    public FieldDto createField(FieldDto fieldDto) {


        fieldValidator.validate(fieldDto);

        fieldRepository.save(fieldMapper.returnField(fieldDto));

       return fieldDto;

    }

    public List<FieldDto> getAllFields() {
       List<Field>  fields= fieldRepository.findAll().stream()
                .collect(Collectors.toList());

       List<FieldDto>fieldDtos=new ArrayList<>();
       for(Field field:fields){

           fieldDtos.add(fieldMapper.returnFieldDto(field));

       }

       return fieldDtos;

    }

    public FieldDto getFieldById(Long id) {


        Field field=fieldRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(" Field not found"));

        return fieldMapper.returnFieldDto(field);

    }

    public FieldDto updateField(FieldDto fieldDto) {
        Field existingField = fieldRepository.getFieldByFieldName(fieldDto.getFieldName())
                .orElseThrow(() -> new EntityNotFoundException("Field not found"));

        fieldValidator.validate(fieldDto);



        existingField.setFieldName(fieldDto.getFieldName());
        existingField.setDescription(fieldDto.getDescription());
        fieldRepository.save(existingField);

        return fieldDto ;
    }

    public String deleteField(Long id) {
        if (!fieldRepository.existsById(id)) {
            throw new EntityNotFoundException("Field not found");
        }
        fieldRepository.deleteById(id);

        return "Field deleted successfully";

    }

    public void initializeFields() {

        List<String> DEFAULT_FIELDS = Arrays.asList(
                "Programming","Marketing","Investing"
        );

        for( String fieldName :DEFAULT_FIELDS)

        {
            // Check if skill already exists
            if (!fieldRepository.existsByFieldNameIgnoreCase(fieldName)) {
                Field field = new Field();
                field.setFieldName(fieldName);
                fieldRepository.save(field);
            }
        }
    }
}