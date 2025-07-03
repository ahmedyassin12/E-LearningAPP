package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "payment_id")
@Table(name = "Payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private long payment_id;

    @Column(name = "amount")
    private Double amount ;


    @Column(name = "paymentDate",nullable = false)
    private LocalDate paymentDate;

    @ManyToOne
    @JoinColumn(name="enrollement_id",nullable = false)
    private Enrollement enrollement ;




}
