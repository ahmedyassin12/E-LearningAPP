package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.demo.Dtos.courseDto.*;
import com.example.demo.dao.CourseDAO;
import com.example.demo.dao.EnrollementDAO;
import com.example.demo.dao.FormationDAO;
import com.example.demo.entity.*;
import com.example.demo.entity.Enums.Role;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.validator.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)

class CourseServiceTest {

    @Mock private CourseDAO courseDAO;
    @Mock private FormationDAO formationDAO;
    @Mock private EnrollementDAO enrollementDAO;
    @Mock private CourseMapper courseMapper;

    @Mock private ObjectValidator<CreateCourseDto> courseValidator ;
    @InjectMocks private CourseService courseService;

    private User mockUser;
    private Course testCourse;
    private Formation testFormation;
    private CourseDto courseDto;
    private ManagerCourseDto managerCourseDto;
    private UnpaidCourseDto unpaidCourseDto;
    private InitiateCourseDto initDto;

    CreateCourseDto createDto ;
    private UsernamePasswordAuthenticationToken authentication;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1L)
                .username("john_doe")
                .email("john@example.com")
                .firstName("John")
                .lastName("Doe")
                .role(Role.STUDENT)
                .password("password")
                .phoneNumber("55555")
                .build();

        testFormation = Formation.builder()
                .id(100L)
                .formationName("Java Fundamentals")
                .date(LocalDate.now())
                .build();

        testCourse = Course.builder()
                .course_id(1L)
                .courseName("Introduction to Java")
                .course_description("Learn Java basics")
                .formation(testFormation)
                .build();


        courseDto = CourseDto.builder()
                .initiateCourseDto(CreateInitiateCourse(testCourse))
                .build();

        managerCourseDto = ManagerCourseDto.builder()
                .initiateCourseDto(CreateInitiateCourse(testCourse))
                .publicId("public123")
                .build();

        unpaidCourseDto = UnpaidCourseDto.builder()
                .initiateCourseDto(CreateInitiateCourse(testCourse))
                .build();
         initDto = InitiateCourseDto.builder()
                .course_id(1L)
                .courseName("Spring Boot")
                .course_description("Advanced Spring topics")
                .build();
         createDto= CreateCourseDto.builder()
                .initiateCourseDto(initDto)
                .formation_id(10L)
                .publicId("abc123")
                .build();


        authentication = new UsernamePasswordAuthenticationToken(mockUser, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // getAllCourses tests
    @Test
    void getAllCourses_ReturnsCourses() {
        when(courseDAO.findAll()).thenReturn(List.of(testCourse));
        when(courseMapper.returnManagerCourseDto(testCourse)).thenReturn(managerCourseDto);

        List<ManagerCourseDto> result = courseService.getAllCourses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(managerCourseDto, result.get(0));
    }

    @Test
    void getAllCourses_NoCourses_ThrowsException() {
        when(courseDAO.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class,
                () -> courseService.getAllCourses());
    }

    // getFormationCourses tests
    @Test
    void getFormationCourses_StudentRolePaid_ReturnsCourseDtos() {
        mockUser.setRole(Role.STUDENT);
        when(courseDAO.getFormationCourses(anyLong())).thenReturn(List.of(testCourse));
        when(enrollementDAO.isEnrollmentPaid(anyLong(), anyLong())).thenReturn(true);
        when(courseMapper.returnCourseDto(any(Course.class))).thenReturn(courseDto);

        Iterable<?> result = courseService.getFormationCourses(100L, authentication);

        assertTrue(result instanceof List);
        List<?> list = (List<?>) result;
        assertEquals(1, list.size());
        assertTrue(list.get(0) instanceof CourseDto);
    }

    @Test
    void getFormationCourses_StudentRoleUnpaid_ReturnsUnpaidDtos() {
        mockUser.setRole(Role.STUDENT);
        when(courseDAO.getFormationCourses(anyLong())).thenReturn(List.of(testCourse));
        when(enrollementDAO.isEnrollmentPaid(anyLong(), anyLong())).thenReturn(false);
        when(courseMapper.returns_UnpaidCourseDto(any(Course.class))).thenReturn(unpaidCourseDto);

        Iterable<?> result = courseService.getFormationCourses(100L, authentication);

        assertTrue(result instanceof List);
        List<?> list = (List<?>) result;
        assertEquals(1, list.size());
        assertTrue(list.get(0) instanceof UnpaidCourseDto);
    }

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

    // getCourseById tests
    @Test
    void getCourseById_ValidId_ReturnsCourseDto() {
        when(courseDAO.findById(anyLong())).thenReturn(Optional.of(testCourse));
        when(courseMapper.returnManagerCourseDto(testCourse)).thenReturn(managerCourseDto);

        ManagerCourseDto result = courseService.getCourseById(1L);

        assertEquals(managerCourseDto, result);
    }

    @Test
    void getCourseById_InvalidId_ThrowsException() {
        when(courseDAO.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> courseService.getCourseById(999L));
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

    // createNewCourse tests
    @Test
    void createNewCourse_ValidRequest_ReturnsCourseDto() {
        CreateCourseDto createDto = new CreateCourseDto();
        InitiateCourseDto initiateCourseDto=InitiateCourseDto.builder()
                .course_description("helo ... ")
                .courseName("c++")
                .build();
        createDto.setInitiateCourseDto(initiateCourseDto);
        createDto.setFormation_id(100L);

        when(formationDAO.findById(100L)).thenReturn(Optional.of(testFormation));
        when(courseMapper.returnCourse(any(CreateCourseDto.class), eq(testFormation)))
                .thenReturn(testCourse);
        when(courseDAO.save(testCourse)).thenReturn(testCourse);
        when(courseMapper.returnCourseDto(testCourse)).thenReturn(courseDto);

        CourseDto result = courseService.createNewCourse(createDto);

        assertEquals(courseDto, result);
        verify(courseDAO).save(testCourse);
    }

    @Test
    void createNewCourse_InvalidDto_ThrowsException() {
        CreateCourseDto createDto = new CreateCourseDto();
        createDto.setFormation_id(999L);


        assertThrows(RuntimeException.class,
                () -> courseService.createNewCourse(createDto));
    }

    // rem_Course tests
    @Test
    void rem_Course_ValidId_DeletesCourse() {
        when(courseDAO.findById(1L)).thenReturn(Optional.of(testCourse));

        courseService.rem_Course(1L);

        verify(courseDAO).deleteById(1L);
    }

    @Test
    void rem_Course_InvalidId_ThrowsException() {
        when(courseDAO.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> courseService.rem_Course(999L));
    }

    // update_Course tests
    @Test
    void update_Course_ValidRequest_ReturnsCourseDto() {

        when(courseDAO.findById(anyLong())).thenReturn(Optional.of(testCourse));
        when(formationDAO.findById(anyLong())).thenReturn(Optional.of(testFormation));
        when(courseMapper.returnCourse(createDto,testFormation))
                .thenReturn(testCourse);
        when(courseDAO.save(testCourse)).thenReturn(testCourse);
        when(courseMapper.returnCourseDto(testCourse)).thenReturn(courseDto);

        CourseDto result = courseService.update_Course(createDto);

        assertEquals(courseDto, result);
        verify(courseDAO).save(testCourse);
    }



    // updatepdf_url tests
    @Test
    void updatepdf_url_ValidCourse_UpdatesSuccessfully() {
        when(courseDAO.findById(testCourse.getCourse_id())).thenReturn(Optional.of(testCourse));

        courseService.updatepdf_url(testCourse.getCourse_id(), "new-pdf.pdf", "public123");

        assertEquals("new-pdf.pdf", testCourse.getCoursePdf_url());
        assertEquals("public123", testCourse.getPublicId());
        verify(courseDAO).save(testCourse);
    }

    @Test
    void updatepdf_url_InvalidCourse_ThrowsException() {
        when(courseDAO.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> courseService.updatepdf_url(999L, "pdf.pdf", "pub123"));
    }

    // getPublicIdFromCourseData tests
    @Test
    void getPublicIdFromCourseData_ValidId_ReturnsPublicId() {
        testCourse.setPublicId("public123");
        when(courseDAO.findById(1L)).thenReturn(Optional.of(testCourse));
        when(courseMapper.returnManagerCourseDto(testCourse)).thenReturn(managerCourseDto);

        String result = courseService.getPublicIdFromCourseData(1L);

        assertEquals("public123", result);
    }

    @Test
    void getPublicIdFromCourseData_NullPublicId_ThrowsException() {
        managerCourseDto.setPublicId(null);
        when(courseDAO.findById(1L)).thenReturn(Optional.of(testCourse));
        when(courseMapper.returnManagerCourseDto(testCourse)).thenReturn(managerCourseDto);


        assertThrows(NullPointerException.class,
                () -> courseService.getPublicIdFromCourseData(1L));
    }

    // updatevideo_url tests
    @Test
    void updatevideo_url_ValidCourse_UpdatesSuccessfully() {
        when(courseDAO.findById(testCourse.getCourse_id())).thenReturn(Optional.of(testCourse));

        courseService.updatevideo_url(testCourse.getCourse_id(), "new-video.mp4", "public456");

        assertEquals("new-video.mp4", testCourse.getCoursevideo_url());
        assertEquals("public456", testCourse.getPublicId());
        verify(courseDAO).save(testCourse);
    }

    @Test
    void updatevideo_url_InvalidCourse_ThrowsException() {
        when(courseDAO.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> courseService.updatevideo_url(999L, "video.mp4", "pub456"));
    }

    private InitiateCourseDto CreateInitiateCourse(Course course ){

        InitiateCourseDto initiateCourseDto =new InitiateCourseDto() ;

        initiateCourseDto.setCourse_id(course.getCourse_id());
        initiateCourseDto.setCourse_description(course.getCourse_description());
        initiateCourseDto.setCourseName(course.getCourseName());

        return initiateCourseDto;


    }
}