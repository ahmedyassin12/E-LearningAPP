package com.example.demo.dao;


import com.example.demo.entity.*;
import com.example.demo.entity.Enums.PaymentStatus;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DataJpaTest
@Transactional
public class FormationDAOTest {

    @Autowired
    private FormationDAO formationDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private EnrollementDAO enrollementDAO;

    @Autowired
    private EntityManager em;

    private Formation testFormation;
    private Formation testFormation2;

    private Formateur formateur;
    private Student student;

    @BeforeEach
    public void setup() {
        formateur = Formateur.builder()

                .firstName("tech")
                .lastName("aziz")
                .email("smlfjkqsmldjf@tech.com")
                .password("smdlfjkqs")
                .phoneNumber("21122458")
                .username("tachtoucha")
                .dateNaissance(LocalDate.now())
                        .build() ;

        em.persist(formateur);
        student = Student.builder()
                .firstName("ssss")
                .lastName("hohoh")
                .email("smlfjkqsmldjf@sqf.com")
                .password("smdlfjkqs")
                .phoneNumber("2112058")
                .username("ss")
                .dateNaissance(LocalDate.now())
                .Student_Grade("A")
                .build();
em.persist(student);

        testFormation = Formation.builder()

                .formationName("Java Fundamentals")
                .description("helelo java classes paw paw")
                .date(LocalDate.now())
                .formateurs(Set.of(formateur))
                .build();
em.persist(testFormation);

        Enrollement enrollement = new Enrollement();
        enrollement.setFormation(testFormation);
        enrollement.setStudent(student);
        enrollement.setPayment_Status(PaymentStatus.Paid);

        em.persist(enrollement);

        em.flush();
        em.clear();
    }

    @Test
    public void testFindFormationByName() {
        Optional<Formation> found = formationDAO.findFormationByName("Java Fundamentals");
        assertThat(found).isPresent();
        assertThat(found.get().getFormationName()).isEqualTo("Java Fundamentals");
    }

    @Test
    public void testFindFormationByNameForFormateur() {
        Optional<Formation> found = formationDAO.findFormationByNameForFormateur("Java Fundamentals", formateur.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getFormationName()).isEqualTo("Java Fundamentals");
    }

    @Test
    public void testGetFormationsForFormateur() {
        testFormation2 = Formation.builder()

                .formationName("new java Formation")
                .description("helelo java classes haw paw")
                .date(LocalDate.now())
                .formateurs(Set.of(formateur))
                .build();
        em.persist(testFormation2);
        Iterable<Formation> formations = formationDAO.getFormationsForFormateur(formateur.getId());
        assertThat(formations).hasSize(2);
        assertThat(formations.iterator().next().getFormationName()).isEqualTo("Java Fundamentals");
    }

    @Test
    public void testGetFormationsforEnrolledStudent() {
        Iterable<Formation> formations = formationDAO.getFormationsforEnrolledStudent(student.getId());
        assertThat(formations).hasSize(1);
        assertThat(formations.iterator().next().getFormationName()).isEqualTo("Java Fundamentals");
    }


}
