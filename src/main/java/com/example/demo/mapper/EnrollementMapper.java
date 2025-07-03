package com.example.demo.mapper;

import com.example.demo.Dtos.enrollementDto.EnrollementDto;
import com.example.demo.entity.Enrollement;
import com.example.demo.entity.Formation;
import com.example.demo.entity.Student;

public class EnrollementMapper {





    public EnrollementDto returnEnrollementDto(Enrollement enrollement){


        return EnrollementDto.builder()
                .enrollementDate(enrollement.getEnrollement_date())
                .enrollementId(enrollement.getEnrollement_id())
                .paymentStatus(enrollement.getPayment_Status())
                .rating(enrollement.getRating())
                .formationName(enrollement.getFormation().getFormationName())
                .studentId(enrollement.getStudent().getId())
                .build();


    }


    public Enrollement returnEnrollement(EnrollementDto enrollementDto, Formation formation, Student student) {

        return Enrollement.builder()
                .enrollement_id(enrollementDto.getEnrollementId())
                .enrollement_date(enrollementDto.getEnrollementDate())
                .formation(formation)
                .student(student)
                .rating(enrollementDto.getRating())
                .payment_Status(enrollementDto.getPaymentStatus())

                .build();



    }
}
