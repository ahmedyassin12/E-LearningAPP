package com.example.demo.entity;
import com.example.demo.entity.Enums.Availability;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.HashSet;
import java.util.Set;



@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuperBuilder
@DiscriminatorValue("Formateur")
@Getter
@Setter
@ToString(exclude = {"events", "skills","formations"}) // Safety for logging

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true) // Look at parent, but only explicit fields


public class Formateur extends User {






    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "formateur_skills",
            joinColumns = @JoinColumn(name = "formateur_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )    private Set<Skill> skills = new HashSet<>();


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Availability availability;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;


    @Column(nullable = false)
    private int experienceYears;

    @ManyToMany(mappedBy = "formateurs",fetch =FetchType.EAGER)
    private Set<Formation> formations = new HashSet<>();

    @ManyToMany(mappedBy = "formateurs")
    private Set<Event> events=new HashSet<>() ;

//additional attributes :











}
