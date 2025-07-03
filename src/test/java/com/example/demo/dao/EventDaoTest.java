package com.example.demo.dao;

import com.example.demo.entity.*;
import com.example.demo.entity.Enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EventDAOTest {

    @Autowired
    private EventDAO eventDAO;

    @Autowired
    private TestEntityManager entityManager;

    private Event event;
    private Formateur formateur;
    private Student student;

    @BeforeEach
    void setup() throws ParseException {
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

         formateur = Formateur.builder()
                .firstName("formateur")
                .lastName("zozo")
                .email("trao@os.com")
                .password("nam")
                .phoneNumber("12321085")
                .username("jugking")
                .dateNaissance(LocalDate.parse(dateString,formatter))
                .experienceYears(2)
                .build();
         entityManager.persist(formateur);


        event = Event.builder()
                .eventName("AI Workshop")
                .date(LocalDate.now())
                .description("Intro to AI")
                .cityLocation("Tunis")
                .streetLocation("Main Street")
                .formateurs(Collections.singleton(formateur))
                .build();

        eventDAO.save(event);

        EventEnrollement enrollement = EventEnrollement.builder()
                .student(student)
                .event(event)
                .Rating(4)
                .build();

        entityManager.persist(enrollement);
    }

    @Test
    void testFindByEventName() {
        Optional<Event> result = eventDAO.getEventByEventName("AI Workshop");
        assertTrue(result.isPresent());
        assertEquals("Tunis", result.get().getCityLocation());
    }

    @Test
    void testFindByStreetLocation() {
        Optional<Event> result = eventDAO.getEventByStreetLocation("Main Street");
        assertTrue(result.isPresent());
        assertEquals("AI Workshop", result.get().getEventName());
    }

    @Test
    void testFindByFormateur() {
        Iterable<Event> result = eventDAO.getEventsForFormateur(formateur.getId());
        assertTrue(result.iterator().hasNext());
    }

    @Test
    void testFindByEventNameForFormateur() {
        Optional<Event> result = eventDAO.findEventByNameForFormateur("AI Workshop", formateur.getId());
        assertTrue(result.isPresent());
    }

    @Test
    void testGetEventForEnrolledStudent() {
        Iterable<Event> result = eventDAO.getEventForEnrolledStudent(student.getId());
        assertTrue(result.iterator().hasNext());
    }
}

