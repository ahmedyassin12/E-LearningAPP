package com.example.demo.dao;

import com.example.demo.entity.*;
import com.example.demo.entity.Enums.PaymentStatus;
import com.example.demo.entity.Enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EnrollementDaoTest {

    @Autowired
    private EnrollementDAO enrollementDAO;

    @Autowired
    private TestEntityManager entityManager;

    private Student student;
    private Formation formation;
    private Enrollement enrollement;

    @BeforeEach
    void setUp() throws ParseException {
        // Create and save a student
        String dateString = "05/12/2002";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        student = Student.builder()
                .firstName("Test")
                .lastName("Student")
                .email("test@student.com")
                .password("secret")
                .phoneNumber("55555555")
                .username("trow")
                .role(Role.STUDENT)
                .age(15)
                .dateNaissance(LocalDate.parse(dateString,formatter))
                .build();
        entityManager.persist(student);

        // Create and save a formation
        formation = Formation.builder()
                .formationName("Spring Boot Course")
                .description("Learn Spring Boot")
                .date(LocalDate.now())
                .build();
        entityManager.persist(formation);

        // Create and save an enrollement
        enrollement = Enrollement.builder()
                .student(student)
                .formation(formation)
                .payment_Status(PaymentStatus.Paid)
                .enrollement_date(LocalDate.now())
                .build();
        entityManager.persist(enrollement);
    }

    @Test
    void testIsStudentEnrolled() {
        //act
        boolean result = enrollementDAO.isStudentEnrolled(student.getId(), formation.getId());


        //assert
        assertTrue(result);

    }

    @Test
    void testIsEnrollmentPaid() {

        boolean result = enrollementDAO.isEnrollmentPaid(student.getId(), formation.getId());
        assertTrue(result);


    }

    @Test
    void testGetEnrollementForStudent() {
        List<Enrollement> result = (List<Enrollement>) enrollementDAO.getEnrollementForStudent(student.getId());
        assertEquals(1, result.size());
        assertEquals(student.getId(), result.get(0).getStudent().getId());
    }

    @Test
    void testGetFormationEnrollement() {
        List<Enrollement> result = (List<Enrollement>) enrollementDAO.getFormationEnrollement(formation.getId());
        assertEquals(1, result.size());
        assertEquals(formation.getId(), result.get(0).getFormation().getId());
    }
}
