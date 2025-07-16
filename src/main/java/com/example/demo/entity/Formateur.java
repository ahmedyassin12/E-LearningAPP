package com.example.demo.entity;
import com.example.demo.entity.Enums.Availability;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import java.util.HashSet;
import java.util.Set;



@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@SuperBuilder
@DiscriminatorValue("Trainer")
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
