package com.example.demo.Dtos.paymentDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePaymentDto {


    @Valid
    @NotNull
    private PaymentDto paymentDto ;


    @NotNull
    private Long enrollement_id ;








}
