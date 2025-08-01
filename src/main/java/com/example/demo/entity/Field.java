package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Field {


    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true,nullable = false)
    private String fieldName;

    private String description;





}

