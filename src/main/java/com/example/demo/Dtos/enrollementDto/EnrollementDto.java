package com.example.demo.Dtos.enrollementDto;

import com.example.demo.entity.Enums.PaymentStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollementDto {

    private Long enrollementId;


    private PaymentStatus paymentStatus;



    private int rating;

    @NotNull
    private LocalDate enrollementDate;

    @NotNull
    private Long studentId;

    @NotEmpty
    private String formationName;


}
