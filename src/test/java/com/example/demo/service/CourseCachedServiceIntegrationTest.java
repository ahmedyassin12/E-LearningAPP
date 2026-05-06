package com.example.demo.service;

import com.example.demo.Dtos.courseDto.*;
import com.example.demo.dao.CourseDAO;
import com.example.demo.dao.EnrollementDAO;
import com.example.demo.dao.FormationDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.entity.*;
import com.example.demo.entity.Enums.Availability;
import com.example.demo.entity.Enums.PaymentStatus;
import com.example.demo.entity.Enums.Role;
import com.example.demo.mapper.CourseMapper;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test") // Uses application-test.properties
public class CourseCachedServiceIntegrationTest {

    @Container
    static final GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);


    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
    }
    @SpyBean private CourseDAO courseDAO;
    @Autowired private EnrollementDAO enrollementDAO;

    @Autowired private UserDAO userDAO;
    @Autowired private FormationDAO formationDAO;
    @Autowired
    private CacheManager cacheManager;

    CourseMapper courseMapper = new CourseMapper();

    // The real service being tested (not a mock)
    @Autowired
    private CourseCacheService courseCacheService;

    private Student student;
    private Course testCourse;
    private Formation testFormation;
    private Enrollement enrollement;
private Formateur formateur1;
    @BeforeEach
    void setUp() {
        // Delete in reverse order of dependencies to avoid Foreign Key constraints
        enrollementDAO.deleteAll();
        courseDAO.deleteAll();
        formationDAO.deleteAll();
        userDAO.deleteAll();

        //clear the cache
        cacheManager.getCache("GetCourses").clear();

        student = Student.builder()
                .id(1L)
                .firstName("jhon")
                .lastName("khan")
                .dateNaissance(LocalDate.parse("2000-02-20"))
                .username("john_doe")
                .password("T9x!qP4@vZ7#fLba1")
                .email("john_doe@gmail.com")
                .role(Role.STUDENT)
                .phoneNumber("21123058")
                .enabled(true)
                .age(15)
                .build();
        userDAO.save(student);

        formateur1 = Formateur.builder()
                .id(1L)
                .firstName("jijo")
                .lastName("kiko")
                .dateNaissance(LocalDate.parse("2004-02-20"))
                .username("jijo mbop")
                .password("T9x!qP4@vZ7#fLba1")
                .email("jijo123@gmail.com")
                .availability(Availability.AVAILABLE)
                .role(Role.FORMATEUR)
                .phoneNumber("21112222")
                .enabled(true)
                .build();
        userDAO.save(formateur1);

        testFormation = Formation.builder()
                .date(LocalDate.now())
                .formateurs(Set.of(formateur1))
                .description("this is Java Formation from start to finish")
                .id(100L)
                .formationName("Java Fundamentals")
                .build();

        formationDAO.save(testFormation);

        testCourse = Course.builder()
                .course_id(1L)
                .course_description("this is jAVA OOP course")
                .formation(testFormation)
                .courseName("Introduction to Java")
                .formation(testFormation)
                .build();

        courseDAO.save(testCourse);


         enrollement=Enrollement.builder()
                .student(student)
                .enrollement_date(LocalDate.now())
                .formation(testFormation)
                .payment_Status(PaymentStatus.Paid)
                .build();
         enrollementDAO.save(enrollement);



    }

    private InitiateCourseDto CreateInitiateCourse(Course course) {
        InitiateCourseDto dto = new InitiateCourseDto();
        dto.setCourse_id(course.getCourse_id());
        dto.setCourseName(course.getCourseName());
        return dto;
    }

    @Test
    void getCachedFormationCourses_StudentRolePaid_ReturnsCourseDtos_VerifyRedisWorkflow() {
        Long formationId = testFormation.getId();

        // --- FIRST CALL: Should hit the DAO (Cache Miss) ---

        // First Execution
        courseCacheService.getCachedFormationCourses(student, formationId);

        // Verify DAO was called once
        verify(courseDAO, times(1)).getFormationCourses(formationId);

        // --- SECOND CALL: Should hit Redis (Cache Hit) ---
        courseCacheService.getCachedFormationCourses(student, formationId);

        // VERIFICATION: The DAO call count should STILL BE 1.
        // If it's 2, the cache is NOT working.
        verify(courseDAO, times(1)).getFormationCourses(formationId);



    }
    @Test
    void getCachedFormationCourses_VerifyRedisWorkflow() {

        Long formationId = testFormation.getId();


        //storing formations's Courses:
        Iterable<?> result= courseCacheService.getCachedFormationCourses(student, formationId);

        //now ill just check if data are correct :
         //convert Iterable into List
        List<?> list = (List<?>) result;

        //check size
        assertEquals(1, list.size());

        //chheck the data type (it should be CourseDto):
        assertTrue(list.get(0) instanceof CourseDto);


    }






    @Test
    void getFormationCourses_StudentRoleUnpaid_ReturnsUnpaidDtos() {

        //change enrollment's status to unpaid :
        enrollement.setPayment_Status(PaymentStatus.UnPaid);
        //save enrollment
        enrollementDAO.save(enrollement);

            Long formationId = testFormation.getId();


            //storing formations's Courses:
            Iterable<?> result= courseCacheService.getCachedFormationCourses(student, formationId);

            //now ill just check if data are correct :

            //convert Iterable into List
            List<?> list = (List<?>) result;

        System.out.println("are we here ??? ");
            //check size
            assertEquals(1, list.size());
        System.out.println("or  we are here ??? ");

            //chheck the data type (it should be CourseDto):
            assertTrue(list.get(0) instanceof UnpaidCourseDto);


        }


        /*
           Next tests to add + continue Caching tests for evict and put :

    @Test
    void getFormationCourses_ManagerRole_ReturnsManagerDtos() {
        mockUser.setRole(Role.MANAGER);
        when(courseDAO.getFormationCourses(anyLong())).thenReturn(List.of(testCourse));
        when(courseMapper.returnManagerCourseDto(any(Course.class))).thenReturn(managerCourseDto);

        Iterable<?> result = courseService.getFormationCourses(100L, authentication);

        assertTrue(result instanceof List);
        List<?> list = (List<?>) result;
        assertEquals(1, list.size());
        assertTrue(list.get(0) instanceof ManagerCourseDto);
    }

    @Test
    void getFormationCourses_FormateurRole_ReturnsCourseDtos() {
        mockUser.setRole(Role.FORMATEUR);
        when(courseDAO.getFormationCourses(anyLong())).thenReturn(List.of(testCourse));
        when(formationDAO.findFormationByNameForFormateur(anyString(), anyLong()))
                .thenReturn(Optional.of(testFormation));
        when(courseMapper.returnCourseDto(any(Course.class))).thenReturn(courseDto);

        Iterable<?> result = courseService.getFormationCourses(100L, authentication);

        assertTrue(result instanceof List);
        List<?> list = (List<?>) result;
        assertEquals(1, list.size());
        assertTrue(list.get(0) instanceof CourseDto);
    }

    @Test
    void getFormationCourses_FormateurRoleUnauthorized_ThrowsException() {
        mockUser.setRole(Role.FORMATEUR);
        when(courseDAO.getFormationCourses(anyLong())).thenReturn(List.of(testCourse));
        when(formationDAO.findFormationByNameForFormateur(anyString(), anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(AccessDeniedException.class,
                () -> courseService.getFormationCourses(100L, authentication));
    }



    @Test
    void getFormationCourses_NoCourses_ThrowsException() {
        when(courseDAO.getFormationCourses(anyLong())).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class,
                () -> courseService.getFormationCourses(100L, authentication));
    }

   // getCourseByName tests
    @Test
    void getCourseByName_StudentRolePaid_ReturnsCourseDto() {
        mockUser.setRole(Role.STUDENT);
        when(courseDAO.findCourseBycourseName(anyString())).thenReturn(Optional.of(testCourse));
        when(enrollementDAO.isEnrollmentPaid(anyLong(), anyLong())).thenReturn(true);
        when(courseMapper.returnCourseDto(testCourse)).thenReturn(courseDto);

        Object result = courseService.getCourseByName("Java", authentication);

        assertTrue(result instanceof CourseDto);
    }

    @Test
    void getCourseByName_StudentRoleUnpaid_ReturnsUnpaidDto() {
        mockUser.setRole(Role.STUDENT);
        when(courseDAO.findCourseBycourseName(anyString())).thenReturn(Optional.of(testCourse));
        when(enrollementDAO.isEnrollmentPaid(anyLong(), anyLong())).thenReturn(false);
        when(courseMapper.returns_UnpaidCourseDto(testCourse)).thenReturn(unpaidCourseDto);

        Object result = courseService.getCourseByName("Java", authentication);

        assertTrue(result instanceof UnpaidCourseDto);
    }


@Test
    void getCourseByName_FormateurRoleUnauthorized_ThrowsException() {
        mockUser.setRole(Role.FORMATEUR);
        when(courseDAO.findCourseBycourseName(anyString())).thenReturn(Optional.of(testCourse));
        when(formationDAO.findFormationByNameForFormateur(anyString(), anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(AccessDeniedException.class,
                () -> courseService.getCourseByName("Java", authentication));
    }



    @Test
    void getCourseByName_CourseNotFound_ThrowsException() {
        when(courseDAO.findCourseBycourseName(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> courseService.getCourseByName("Invalid", authentication));
    }

        @Test
    void getCourseByName_ManagerRole_ReturnsManagerDto() {
        mockUser.setRole(Role.MANAGER);
        when(courseDAO.findCourseBycourseName(anyString())).thenReturn(Optional.of(testCourse));
        when(courseMapper.returnManagerCourseDto(testCourse)).thenReturn(managerCourseDto);

        Object result = courseService.getCourseByName("Java", authentication);

        assertTrue(result instanceof ManagerCourseDto);
    }

    @Test
    void getCourseByName_FormateurRole_ReturnsCourseDto() {
        mockUser.setRole(Role.FORMATEUR);
        when(courseDAO.findCourseBycourseName(anyString())).thenReturn(Optional.of(testCourse));
        when(formationDAO.findFormationByNameForFormateur(anyString(), anyLong()))
                .thenReturn(Optional.of(testFormation));
        when(courseMapper.returnCourseDto(testCourse)).thenReturn(courseDto);

        Object result = courseService.getCourseByName("Java", authentication);

        assertTrue(result instanceof CourseDto);
    }


         */
}