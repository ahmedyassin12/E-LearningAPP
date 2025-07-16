package com.example.demo.dao;


import com.example.demo.entity.*;
import com.example.demo.entity.Enums.Availability;
import com.example.demo.entity.Enums.PaymentStatus;
import com.example.demo.entity.Enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)

class CourseDAOTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseDAO courseDAO;

    @Test
    void findCourseByCourseName_ShouldReturnCourse() throws ParseException {
        Formateur formateur =createAndPersistFormateur() ;

        // Given
        Formation formation = createAndPersistFormation(formateur);
        Course course = new Course();
        course.setPublicId("hhhh");
        course.setCourse_description("helo helo "); ;
        course.setCourseName("Spring Boot");
        course.setFormation(formation);
        entityManager.persist(course);

        // When
        Optional<Course> found = courseDAO.findCourseBycourseName("Spring Boot");

        // Then
        assertTrue(found.isPresent());
        assertEquals("Spring Boot", found.get().getCourseName());
    }

    @Test
    void getFormateurCourses_ShouldReturnCoursesForFormateurAndFormation() throws ParseException {
        // Given
        Formateur formateur =createAndPersistFormateur();
        entityManager.persist(formateur);

        Formation formation = createAndPersistFormation(formateur);
        formation.setFormateurs(new HashSet<>(Collections.singleton(formateur)));
        entityManager.persist(formation);

        Course course = createAndPersistCourse(formation);

        // When
        Iterable<Course> courses = courseDAO.getFormateurCourses(formateur.getId(), formation.getId());

        // Then
        assertTrue(courses.iterator().hasNext());
        assertEquals(course.getCourse_id(), courses.iterator().next().getCourse_id());
    }

    @Test
    void findCoursesByStudentAndFormation_ShouldReturnPaidCourses() throws ParseException {
        // Given
        Formateur formateur =createAndPersistFormateur();

        Student student = createAndPersistStudent();
        Formation formation = createAndPersistFormation(formateur);
        Enrollement enrollement = createAndPersistEnrollement(student, formation, PaymentStatus.Paid);
        Course course = createAndPersistCourse(formation);

        // When
        Iterable<Course> courses = courseDAO.findCoursesByStudentAndFormation(
                student.getId(),
                formation.getId()
        );

        // Then
        assertTrue(courses.iterator().hasNext());
        assertEquals(course.getCourse_id(), courses.iterator().next().getCourse_id());
    }

    @Test
    void getFormationCourses_ShouldReturnAllCoursesForFormation() throws ParseException {
        // Given
        Formateur formateur =createAndPersistFormateur();

        Formation formation = createAndPersistFormation(formateur);
        Course course1 = createAndPersistCourse(formation);
        Course course2 = createAndPersistCourse(formation);
        createAndPersistEnrollement(createAndPersistStudent(), formation, PaymentStatus.Paid);

        // When
        Iterable<Course> courses = courseDAO.getFormationCourses(formation.getId());

        // Then
        List<Course> result = (List<Course>) courses;
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(c -> c.getCourse_id() == course1.getCourse_id()));
        assertTrue(result.stream().anyMatch(c -> c.getCourse_id() == course2.getCourse_id()));
    }




    // Helper methods
    private Formation createAndPersistFormation(Formateur formateur) {
        Formation formation = Formation.builder()
                .formationName("Java Fundamentals")
                .date(LocalDate.now())
                .formateurs(Collections.singleton(formateur))
                .build();
        return entityManager.persist(formation);
    }
    private Formateur createAndPersistFormateur() throws ParseException {
        String dateString = "05/12/2002";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        Formateur formateur = Formateur.builder()
                .firstName("formateur")
                .lastName("zozo")
                .email("trao@os.com")
                .password("nam")
                .phoneNumber("12321085")
                .username("jugking")
                .dateNaissance(LocalDate.parse(dateString,formatter))
                .experienceYears(2)
                .availability(Availability.AVAILABLE)
                .build();
        return entityManager.persist(formateur);
    }

    private Course createAndPersistCourse(Formation formation) {
        Course course = Course.builder()
                .courseName("Java Basics")
                .formation(formation)
                .build();
        return entityManager.persist(course);
    }

    private Student createAndPersistStudent() {
        Student student = Student.builder()
                .firstName("hoho").lastName("5akj").role(Role.STUDENT)
                .email("sqfodhf@sdfl.com").username("qsmf123").phoneNumber("25623360")
                .password("123456789")
                .age(11)
                .build();
        return entityManager.persist(student);
    }

    private Enrollement createAndPersistEnrollement(Student student, Formation formation, PaymentStatus paymentStatus) {
        Enrollement enrollement = Enrollement.builder()
                .student(student)
                .formation(formation)
                .payment_Status(paymentStatus)
                .enrollement_date(LocalDate.now())
                .build();
        return entityManager.persist(enrollement);
    }
}