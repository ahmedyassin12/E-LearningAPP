package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long event_id;

    @Column(name = "eventName",unique = true)
    private String eventName;

    @Column(name = "date",nullable = false)
    private LocalDate date;

    @Column(name = "description")
    private String description;

    @Column(name = "cityLocation")
    private String cityLocation;
    @Column(name = "streetLocation")
    private String streetLocation;


    @Column(name="imageUrl")
    private String imageUrl ;

    @Column(name="Public_id")
    private String publicId ;


    @OneToMany(mappedBy = "event")
    private Set<EventEnrollement> enrollements = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "formateur_event",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "formateur_id"))
    private Set <Formateur> formateurs ;




}
