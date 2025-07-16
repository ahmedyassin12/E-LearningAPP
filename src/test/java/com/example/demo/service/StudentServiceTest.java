package com.example.demo.service;

import com.example.demo.Dtos.userDto.CreateStudentDto;
import com.example.demo.Dtos.userDto.StudentDto;
import com.example.demo.auth.Service.AuthenticationService;
import com.example.demo.dao.StudentDAO;
import com.example.demo.entity.Enums.Role;
import com.example.demo.entity.Student;
import com.example.demo.mapper.UsersMapper;
import com.example.demo.validator.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentDAO studentDAO;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UsersMapper studentMapper;

    @Mock
    private ObjectValidator<CreateStudentDto> studentValidator ;

    @Mock
    private ObjectValidator<StudentDto> updateStudentValidator ;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentDto studentDto;
    private CreateStudentDto createStudentDto;

    @BeforeEach
    void setUp() {
        student=Student.builder()
                .id(1L)
                .username("john_doe")
                .email("john@example.com")
                .firstName("jhon")
                .lastName("oo")
                .role(Role.STUDENT)
                .password("password")
                .phoneNumber("55550")
                .build();

        studentDto = StudentDto.builder()
                .id(1L)
                .username("john_doe")
                .email("john@example.com")
                .firstName("jhon")
                .lastName("oo")
                .role(Role.STUDENT)
                .phoneNumber("55550")
                .build();

        createStudentDto =CreateStudentDto.builder()
                .id(1L)
                .username("john_doe")
                .email("john@example.com")
                .firstName("jhon")
                .lastName("oo")
                .role(Role.STUDENT)
                .password("password")
                .phoneNumber("55550")
                .publicId("qsfoq")
                .build();


    }

    @Test
    void getAllStudents_ReturnsList() {

        List<Student> students = Collections.singletonList(student);
        when(studentDAO.findAll()).thenReturn(students);

        when(studentMapper.returnStudentDto(student)).thenReturn(studentDto) ;

        List<StudentDto> result = studentService.getAllStudents();

        assertEquals(1, result.size());
        assertEquals(student.getId(), result.get(0).getId());


    }


    @Test
    void getStudentOfFormation_ValidId_ReturnsList() {
        Long formationId = 1L;
        when(studentDAO.getStudentsByFormation(formationId))
                .thenReturn(Collections.singletonList(student));
        when(studentMapper.returnStudentDto(student)).thenReturn(studentDto) ;

        List<StudentDto> result = studentService.getStudentOfFormation(formationId);

        assertEquals(1, result.size());
        assertEquals(student.getId(), result.get(0).getId());
    }

    @Test
    void getStudentByEmail_ValidEmail_ReturnsDto() {
        String email = "john@example.com";
        when(studentDAO.findStudentByEmail(email)).thenReturn(Optional.of(student));
        when(studentMapper.returnStudentDto(student)).thenReturn(studentDto) ;

        StudentDto result = studentService.getStudentByEmail(email);

        assertEquals(email, result.getEmail());
    }
    @Test
    void getStudentOfFormation_InvalidId_ThrowsException() {
        Long formationId = 99L;
        when(studentDAO.getStudentsByFormation(formationId)).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class,
                () -> studentService.getStudentOfFormation(formationId));
    }
    @Test
    void getStudentByEmail_InvalidEmail_ThrowsException() {
        String email = "invalid@email.com";
        when(studentDAO.findStudentByEmail(email)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> studentService.getStudentByEmail(email));
    }

    @Test
    void createNewStudent_Success_ReturnsDto() {
        UsersMapper realMapper = new UsersMapper();
        String encodedPassword = "encoded_password";

        when(passwordEncoder.encode(createStudentDto.getPassword()))
                .thenReturn(encodedPassword);
        when(studentMapper.returnStudent(createStudentDto)).thenReturn(student) ;
        when(studentMapper.returnStudentDto(student)).thenReturn(studentDto) ;

        when(studentDAO.save(any(Student.class))).thenReturn(student);

        StudentDto result = studentService.createNewStudent(createStudentDto);

        verify(passwordEncoder).encode("password");
        verify(studentDAO).save(any(Student.class));
        assertEquals(1L, result.getId());
        assertEquals("john@example.com", result.getEmail());

    }

    @Test
    void remStudent_ValidId_DeletesStudent() {
        Long id = 1L;
        when(studentDAO.findById(id)).thenReturn(Optional.of(student));

        String result = studentService.rem_student(id);

        verify(studentDAO).deleteById(id);
        assertEquals("Student deleted successfully ", result);
    }

    @Test
    void remStudent_InvalidId_ThrowsException() {
        Long id = 99L;
        when(studentDAO.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> studentService.rem_student(id));
    }

    @Test
    void updateStudentImage_ValidId_UpdatesImage() {
        Long studentId = 1L;
        String imageUrl = "url";
        String publicId = "public_id";

        when(studentDAO.findById(studentId)).thenReturn(Optional.of(student));

        studentService.updateStudentImage(studentId, imageUrl, publicId);

        assertEquals(imageUrl, student.getImageUrl());
        assertEquals(publicId, student.getPublicId());
        verify(studentDAO).save(student);
    }

    @Test
    void updateStudentImage_InvalidId_ThrowsException() {
        Long studentId = 99L;
        when(studentDAO.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> studentService.updateStudentImage(studentId, "url", "public_id"));
    }


}