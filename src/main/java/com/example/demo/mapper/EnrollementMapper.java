package com.example.demo.mapper;

import com.example.demo.Dtos.enrollementDto.EnrollementDto;
import com.example.demo.entity.Enrollement;
import com.example.demo.entity.Formation;
import com.example.demo.entity.Student;
import jakarta.validation.constraints.Null;

public class EnrollementMapper {





    public EnrollementDto returnEnrollementDto(Enrollement enrollement){



        EnrollementDto enrollementDto= EnrollementDto.builder()
                .enrollementDate(enrollement.getEnrollement_date())
                .enrollementId(enrollement.getEnrollement_id())
                .paymentStatus(enrollement.getPayment_Status())
                .formationName(enrollement.getFormation().getFormationName())
                .studentId(enrollement.getStudent().getId())
                .build();
        if(enrollement.getRating()!=null)enrollementDto.setRating(enrollement.getRating());

        return enrollementDto;

    }


    public Enrollement returnEnrollement(EnrollementDto enrollementDto, Formation formation, Student student) {

        Enrollement enrollement= Enrollement.builder()
                .enrollement_id(enrollementDto.getEnrollementId())
                .enrollement_date(enrollementDto.getEnrollementDate())
                .formation(formation)
                .student(student)
                .payment_Status(enrollementDto.getPaymentStatus())
                .build();

        if(enrollementDto.getRating() != null ) enrollement.setRating(enrollementDto.getRating());

return enrollement;

    }
}
