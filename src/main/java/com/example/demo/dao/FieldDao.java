package com.example.demo.dao;

import com.example.demo.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FieldDao extends JpaRepository<Field, Long> {



    Optional<Field> getFieldByFieldName(String fieldName);


    boolean existsByFieldNameIgnoreCase(String fieldName);


}
