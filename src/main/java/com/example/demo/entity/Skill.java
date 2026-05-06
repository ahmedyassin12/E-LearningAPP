package com.example.demo.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@ToString(exclude = {"formateurs"}) // Safety for logging
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Safety for Sets/Maps
public class Skill {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true,nullable = false)
    private String name;


    @ManyToMany(mappedBy = "skills", fetch = FetchType.LAZY)
    private Set<Formateur> formateurs;





}
