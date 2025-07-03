package com.example.demo.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@SuperBuilder
@DiscriminatorValue("STUDENT")
public class Student extends User {


    @Column(name="imageUrl")
    private String imageUrl ;

    @Column(name="Public_id")
    private String publicId ;


    @Column(name="age")
   @Min(10)
    private int age ;







//additional attributes :



}

