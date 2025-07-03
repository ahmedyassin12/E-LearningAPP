package com.example.demo.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
public class Formation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "FormationName",unique = true,nullable = false)
    private String formationName;

    @Column(name = "Description")
    private String description;
    @Column(name = "date",nullable = false)
    private LocalDate date;

    @Column(name="imageUrl")
    private String imageUrl ;

    @Column(name="Public_id")
    private String publicId ;



    @OneToMany(mappedBy = "formation",cascade = CascadeType.PERSIST)
    private Set<Enrollement> enrollements=new HashSet<>();

    @Column(nullable = false)
    @ManyToMany()
    @JoinTable(name = "formateur_formation",
            joinColumns = @JoinColumn(name = "formation_id"),
            inverseJoinColumns = @JoinColumn(name = "formateur_id"))
    private Set <Formateur> formateurs ;





    @OneToMany(mappedBy = "formation",cascade = CascadeType.ALL)
    private Set<Course> courses=new HashSet<>() ;





}
