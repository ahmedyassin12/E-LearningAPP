package com.example.demo.controller;
import com.example.demo.Dtos.paymentDto.CreatePaymentDto;
import com.example.demo.Dtos.paymentDto.PaymentDto;
import com.example.demo.entity.Payment;
import com.example.demo.service.EnrollementService;
import com.example.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.InvalidRoleInfoException;
import java.security.Principal;
import java.util.List;

@RestController
    @RequestMapping("/api/v9/Paiement")

public class PaiementController {



    @Autowired
   private PaymentService paymentService;

    @Autowired
    private EnrollementService enrollementService;

    @GetMapping("/getAllPaiements")
    public ResponseEntity<Iterable<PaymentDto>> getAllPaiements() {

        Iterable<PaymentDto> paymentDtos = paymentService.getAllPayments() ;

        return new ResponseEntity<>(paymentDtos, HttpStatus.OK);



    }
    @GetMapping("/getPaiementsOfEnrollement/{enrollementId}")

    public ResponseEntity<Iterable<PaymentDto> >getPaiementsOfEnrollement(
            @PathVariable Long enrollementId,
            Principal connectedUser
    ) throws InvalidRoleInfoException {


return new ResponseEntity<>(paymentService.getPaymentsOfEnrollement(enrollementId,connectedUser),HttpStatus.OK) ;


    }


    @GetMapping("/getPaiementByStudent_id/{student_id}")
    public ResponseEntity<List<PaymentDto>> getPaymentByStudent_id(@PathVariable("student_id") int student_id ) {

        List<PaymentDto> paymentDtos = paymentService.getPaymentByStudent_id(student_id);

        return new ResponseEntity<>(paymentDtos, HttpStatus.OK);

    }

    @GetMapping("/getPaiementById/{id}")
    public ResponseEntity<PaymentDto> getPaiementById(@PathVariable("id") long id) {

        PaymentDto paymentDto = paymentService.getpaymentById(id);
        return new ResponseEntity<>(paymentDto, HttpStatus.OK);

    }

    @PostMapping({"/createPaiement"})
    public ResponseEntity<PaymentDto> createPayment(@RequestBody CreatePaymentDto createPaymentDto) {





        return new ResponseEntity<>(paymentService.createPayment(createPaymentDto), HttpStatus.OK);



    }
    @DeleteMapping("/deletePaiement/{payment_id}")

    public ResponseEntity<String> rem_student(@PathVariable("payment_id")  long payment_id){

        paymentService.deletePayment(payment_id);

        return new ResponseEntity<>(HttpStatus.OK) ;


    }

    @PutMapping("/updatePaiement")
    public  ResponseEntity<PaymentDto> updatePaiement(@RequestBody CreatePaymentDto createPaymentDto){


        return new ResponseEntity<>(paymentService.updatePayment(createPaymentDto),HttpStatus.OK);

    }













}
