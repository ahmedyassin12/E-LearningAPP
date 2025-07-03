package com.example.demo.mapper;

import com.example.demo.Dtos.paymentDto.CreatePaymentDto;
import com.example.demo.Dtos.paymentDto.PaymentDto;
import com.example.demo.entity.Enrollement;
import com.example.demo.entity.Payment;

public class PaymentMapper {




    public Payment returnPayment(CreatePaymentDto createPaymentDto , Enrollement enrollement){


        return Payment.builder()

                .payment_id(createPaymentDto.getPaymentDto().getPayment_id())
                .paymentDate(createPaymentDto.getPaymentDto().getPaymentDate())
                .amount(createPaymentDto.getPaymentDto().getAmount())
                .enrollement(enrollement)

                .build();


    }

    public PaymentDto returnPaymentDto(Payment payment){

        return PaymentDto.builder()

                .amount(payment.getAmount())
                .payment_id(payment.getPayment_id())
                .paymentDate(payment.getPaymentDate())

                .build();


    }




}
