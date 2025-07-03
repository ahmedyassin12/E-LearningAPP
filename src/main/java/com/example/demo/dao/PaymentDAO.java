package com.example.demo.dao;

import com.example.demo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository

public interface PaymentDAO extends JpaRepository<Payment, Long> {

/*
    List<Paiement> findByEnrollement_EnrollementId(Long enrollementId);*/


    @Query("SELECT p " +
            "FROM Payment p " +
            "JOIN p.enrollement e " +
            "WHERE e.student.id = :studentId")
    List<Payment> findByStudentId(@Param("studentId") long studentId);

    @Query("SELECT MAX(p.paymentDate) " +
            "FROM Payment p " +
            "WHERE p.enrollement.enrollement_id = :enrollementId")
    LocalDate findLastPaymentDateByEnrollement(@Param("enrollementId") Long enrollementId);


    @Query("SELECT p FROM Payment p WHERE p.enrollement.enrollement_id = :enrollementId")
    Iterable<Payment> getPaiementsOfEnrollement(@Param("enrollementId") Long enrollementId);

    @Query("SELECT p FROM Payment p WHERE p.enrollement.enrollement_id = :enrollementId " +
            "and p.enrollement.student.id = :studentId")
    Iterable<Payment> getPaiementsOfEnrollementOfStudent(@Param("enrollementId") Long enrollementId
            ,@Param("studentId") Long studentId);


}

