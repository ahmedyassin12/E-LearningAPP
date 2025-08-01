package com.example.demo.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Skill {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true,nullable = false)
    private String name;


    @ManyToMany(mappedBy = "skills", fetch = FetchType.LAZY)
    private Set<Formateur> formateurs;





}
