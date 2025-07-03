package com.example.demo.Dtos.paymentDto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDto {



    private long payment_id;

    @NotNull
    private Double amount ;


    @NotNull
    private LocalDate paymentDate;





}
