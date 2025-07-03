        package com.example.demo.entity;
        import jakarta.persistence.*;
        import lombok.*;

        @Entity
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @ToString
        public class Course {

            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            @Column(name = "course_id")
            private long course_id;


            @Column(name = "course_name")
            private String courseName;


            @Column(name = "course_description")
            private String course_description;

            @Column(name = "VideoUrl")
            private String coursevideo_url;

            @Column(name = "PdfUrl")
            private String coursePdf_url;

            @Column(name="public_id")
            private String publicId ;





            @ManyToOne
            @JoinColumn(name ="Formation_id" ,nullable = false)
            private Formation formation ;





        }
