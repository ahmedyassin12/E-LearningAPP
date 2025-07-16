    package com.example.demo.entity;

    import com.example.demo.entity.Enums.PaymentStatus;
    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDate;
    import java.util.List;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    @Table(uniqueConstraints = {
            @UniqueConstraint(columnNames = {"student_id", "formation_id"}),
    })

    public class Enrollement {



        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "enrollement_id")
        private Long enrollement_id ;

        @Enumerated(EnumType.STRING)
        @Column(name="payment_Status")
        private PaymentStatus payment_Status;

        @Column(name="rating")
        private Integer rating ;



        @Column(name = "Enrollement_date",nullable = false)
        private LocalDate enrollement_date;



        @OneToMany(mappedBy = "enrollement",fetch = FetchType.EAGER,cascade =CascadeType.ALL)
        private List <Payment> payment;

        @JsonIgnoreProperties({"student"}) // Prevent infinite recursion during serialization
        @ManyToOne
        @JoinColumn(name = "student_id", nullable = false)
        private Student student;        

        @ManyToOne
        @JoinColumn(name = "Formation_id")
        private Formation formation;





    }
