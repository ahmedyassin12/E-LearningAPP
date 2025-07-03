package com.example.demo.dao;

import com.example.demo.entity.*;
import com.example.demo.entity.Enums.PaymentStatus;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional
class PaymentDAOTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private PaymentDAO paymentDAO;

    private Student student;
    private Enrollement enrollement;
    private Formation testFormation;

    @BeforeEach
    void setUp() {
        // Create and persist common entities
        student = Student.builder()
                .firstName("ssss")
                .lastName("hohoh")
                .email("smlfjkqsmldjf@sqf.com")
                .password("smdlfjkqs")
                .phoneNumber("2112058")
                .username("ss")
                .dateNaissance(LocalDate.now())
                .Student_Grade("A")
                .age(15)
                .build();
        em.persist(student);


        testFormation = Formation.builder()

                .formationName("Java Fundamentals")
                .description("helelo java classes paw paw")
                .date(LocalDate.now())
                .build();
        em.persist(testFormation);

         enrollement = new Enrollement();
        enrollement.setFormation(testFormation);
        enrollement.setStudent(student);
        enrollement.setPayment_Status(PaymentStatus.Paid);

        em.persist(enrollement);

    }






    @Test
    void findByStudentId_ShouldReturnPaymentsForStudent() {
        // Arrange
        Payment payment = createTestPayment();
        em.persist(payment);
      //  em.flush();

        // Act
        List<Payment> result = paymentDAO.findByStudentId(student.getId());

        // Assert
        Assertions.assertThat(result).isNotNull() ;
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getPaymentDate()).isEqualTo(payment.getPaymentDate()) ;
        Assertions.assertThat(result.get(0).getAmount()).isEqualTo(payment.getAmount()) ;

    }














    @Test
    void getPaiementsOfEnrollement_ShouldReturnAllPayments() {
        // Arrange
       Payment  payment1= em.persist(createTestPayment());
        Payment payment2=em.persist(createTestPayment());
        payment2.setPaymentDate(LocalDate.now().minusDays(5));

        em.flush();

        // Act
        Iterable<Payment> result = paymentDAO.getPaiementsOfEnrollement(enrollement.getEnrollement_id());

        // Assert
        Assertions.assertThat(result).isNotNull() ;
        Assertions.assertThat(result).hasSize(2);

        List<Payment>payments=(List<Payment>) result ;

            Assertions.assertThat(payments.get(0).getPaymentDate()).isEqualTo(payment1.getPaymentDate());
            Assertions.assertThat(payments.get(0).getAmount()).isEqualTo(payment1.getAmount());


        Assertions.assertThat(payments.get(1).getPaymentDate()).isEqualTo(payment2.getPaymentDate());
        Assertions.assertThat(payments.get(1).getAmount()).isEqualTo(payment2.getAmount());


    }

    @Test
    void findByStudentId_WhenNoPayments_ShouldReturnEmptyList() {
        // Act
        List<Payment> result = paymentDAO.findByStudentId(student.getId());

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void findLastPaymentDateByEnrollement_WhenNoPayments_ShouldReturnNull() {
        // Arrange
        Payment payment0 = createTestPayment();
        payment0.setPaymentDate(LocalDate.now());
        em.persist(payment0);
        Payment payment = createTestPayment();
        payment.setPaymentDate(LocalDate.now().minusMonths(3));
        em.persist(payment);
        Payment payment3 = createTestPayment();
        payment3.setPaymentDate(LocalDate.now().minusMonths(5));
        em.persist(payment3);
        Payment payment2 = createTestPayment();
        payment2.setPaymentDate(LocalDate.now().minusMonths(2));
        em.persist(payment2);
        // Act
        LocalDate result = paymentDAO.findLastPaymentDateByEnrollement(enrollement.getEnrollement_id());

        // Assert
        assertThat(result).isEqualTo(LocalDate.now());
    }

    private Payment createTestPayment() {
        return Payment.builder()
                .amount(100.0)
                .paymentDate(LocalDate.now())
                .enrollement(enrollement)
                .build();
    }
}


