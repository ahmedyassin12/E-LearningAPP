package com.example.demo.dao;

import com.example.demo.entity.*;
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

import static org.junit.jupiter.api.Assertions.*;



@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class EventEnrollementDAOTest {

    @Autowired
    private EventEnrollementDAO  eventEnrollementDAO;

    @Autowired
    private TestEntityManager entityManager;

    private Student student;
    private Event event;
    private EventEnrollement  enrollement;

    @BeforeEach
    void setUp() throws ParseException {
        // Create and save a student
        String dateString = "05/12/2002";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateString);


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
        event = Event.builder()
                .eventName("Spring Boot Event")
                .description("Learn Spring Boot")
                .date(LocalDate.now())
                .build();
        entityManager.persist(event);

        // Create and save an enrollement
        enrollement = EventEnrollement.builder()
                .student(student)
                .event(event)
                .enrollementDate(LocalDate.now())
                .build();
        entityManager.persist(enrollement);
    }



    @Test
    void testGetEventEnrollementForStudent() {
        var results = eventEnrollementDAO.getEventEnrollementForStudent(student.getId());
        assertTrue(results.iterator().hasNext(), "Expected enrollement(s) for student");
    }

    @Test
    void testGetEventEnrollementByEventId() {
        var results = eventEnrollementDAO.getEventEnrollement(event.getEvent_id());
        assertTrue(results.iterator().hasNext(), "Expected enrollement(s) for event");
    }

    @Test
    void testIsStudentEnrolled() {
        boolean enrolled = eventEnrollementDAO.isStudentEnrolled(student.getId(), event.getEvent_id());
        assertTrue(enrolled, "Student should be marked as enrolled");
    }

    @Test
    void testIsStudentNotEnrolled() {
        boolean enrolled = eventEnrollementDAO.isStudentEnrolled(999L, event.getEvent_id());
        assertFalse(enrolled, "Unknown student should not be enrolled");
    }
}