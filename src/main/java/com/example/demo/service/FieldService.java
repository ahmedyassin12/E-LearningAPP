package com.example.demo.service;

import com.example.demo.dao.FieldDao;

import com.example.demo.entity.Field;

import com.example.demo.entity.Skill;
import com.example.demo.validator.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FieldService {

    private final FieldDao fieldRepository;
    private final ObjectValidator<Field> fieldValidator;

    public Field createField(Field field) {

        if (fieldRepository.existsByFieldNameIgnoreCase(field.getFieldName())) {

            throw new IllegalArgumentException("Field"+field.getFieldName()+" already exists") ;
        }
        fieldValidator.validate(field);

       return fieldRepository.save(field);

    }

    public List<Field> getAllFields() {
        return fieldRepository.findAll().stream()
                .collect(Collectors.toList());

    }

    public Field getFieldById(Long id) {
        return fieldRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Field not found"));
    }

    public Field updateField(Field field) {
        Field existingField = fieldRepository.findById(field.getId())
                .orElseThrow(() -> new EntityNotFoundException("Field not found"));

        fieldValidator.validate(field);



        existingField.setFieldName(field.getFieldName());
        existingField.setDescription(field.getDescription());

        return fieldRepository.save(existingField);
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