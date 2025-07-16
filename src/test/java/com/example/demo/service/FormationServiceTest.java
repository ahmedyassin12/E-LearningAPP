package com.example.demo.service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.demo.Dtos.formationDto.CreateFormationDto;
import com.example.demo.Dtos.formationDto.FormationDto;
import com.example.demo.Dtos.formationDto.FormationManagerDto;
import com.example.demo.dao.FormateurDAO;
import com.example.demo.entity.Enums.Availability;
import com.example.demo.entity.Enums.Role;
import com.example.demo.validator.ObjectValidator;
import org.assertj.core.api.Assertions ;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.Dtos.formationDto.FormationStudentDto;
import com.example.demo.dao.EnrollementDAO;
import com.example.demo.dao.FormationDAO;
import com.example.demo.entity.*;
import com.example.demo.mapper.FormationMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;

import java.time.LocalDate;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class FormationServiceTest {

    @Mock private FormationDAO formationDAO;
    @Mock private EnrollementDAO enrollementDAO;
    @Mock private FormationMapper formationMapper;

    @Mock
    private ObjectValidator<CreateFormationDto> formationValidator ;

    @Mock private FormateurDAO formateurDAO ;
    @InjectMocks private FormationService formationService;

    private User mockUser;
    private Formation testFormation;
    private FormationDto formationDto ;
    UsernamePasswordAuthenticationToken authentication ;
    SecurityContext securityContext = new SecurityContextImpl();

    @BeforeEach
    void setUp() {
        mockUser=User.builder()
                .id(1L)
                .username("john_doe")
                .email("john@example.com")
                .firstName("jhon")
                .lastName("oo")
                .role(Role.STUDENT)
                .password("password")
                .phoneNumber("55550")
                .build();

        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        testFormation = Formation.builder()

                .id(1L)
                .formationName("Java Fundamentals")
                .description("helelo java classes paw paw")
                .date(LocalDate.now())
                .build();

        formationDto = FormationDto.builder()
                .formation_name(testFormation.getFormationName())
                .formation_id(testFormation.getId())
                .description(testFormation.getDescription())
                .date(testFormation.getDate())
                .imageUrl(testFormation.getImageUrl())
                .build() ;

    }


    @Test
    void getFormationsForFormateur_ReturnsFormations() {
        // Arrange
        FormationDto expectedDto = formationDto.builder()
                .formation_id(testFormation.getId())
                .formation_name(testFormation.getFormationName())
                .description(testFormation.getDescription())
                .date(testFormation.getDate())
                .imageUrl(testFormation.getImageUrl())
                .build();

        mockUser.setRole(Role.FORMATEUR);

        authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null);

        when(formationDAO.getFormationsForFormateur(mockUser.getId()))
                .thenReturn(List.of(testFormation));

        when(formationMapper.returnformationDto(eq(testFormation)))
                .thenReturn(expectedDto);

        // Act
        Iterable<?> result = formationService.getFormationsForFormateur(authentication);

        // Assert

        assertNotNull(result);
        assertTrue(result.iterator().hasNext());

        Object firstItem = result.iterator().next();
        assertTrue(firstItem instanceof FormationDto);

        FormationDto dto = (FormationDto) firstItem;
        assertEquals(expectedDto.getFormation_id(), dto.getFormation_id());
        assertEquals(expectedDto.getFormation_name(), dto.getFormation_name());
        assertEquals(expectedDto.getDescription(), dto.getDescription());
    }
    // getAllFormation tests
    @Test
    void getAllFormation_ManagerRole_ReturnsFormations() {
        mockUser.setRole(Role.MANAGER);
        authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null);
        FormationManagerDto expectedDto = FormationManagerDto.builder()
                .formation_id(testFormation.getId())
                .formation_name(testFormation.getFormationName())
                .description(testFormation.getDescription())
                .date(testFormation.getDate())
                .imageUrl(testFormation.getImageUrl())
                .publicId(testFormation.getPublicId())
                .build();

        when(formationDAO.findAll()).thenReturn(List.of(testFormation) );
        when(formationMapper.returnformationManagerDto(testFormation)).thenReturn(expectedDto);

        Iterable<?> result = formationService.getAllFormation(authentication);

        assertTrue(result.iterator().hasNext());
        assertEquals(expectedDto, result.iterator().next());


    }



    @Test
    void getAllFormation_StudentRole_ReturnsDTOs() {

        FormationStudentDto testFormationDto = FormationStudentDto.builder()
                .formation_name("Java Fundamentals")
                .formation_id(testFormation.getId())
                .isStudentEnrolled(true)
                .IsEnrollementpaid(true)
                .date(LocalDate.now())
                .build();


        authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null);

        when(formationDAO.findAll()).thenReturn((Iterable) List.of(testFormation) );
        when(enrollementDAO.isStudentEnrolled(mockUser.getId(), testFormation.getId())).thenReturn(true);
        when(enrollementDAO.isEnrollmentPaid(mockUser.getId(), testFormation.getId())).thenReturn(true);
        when(formationMapper.returnformationStudentDto(testFormation, true, true))
                .thenReturn(testFormationDto);

        Iterable<?> result = formationService.getAllFormation(authentication);

        assertTrue(result.iterator().hasNext());
        verify(formationMapper).returnformationStudentDto(any(), anyBoolean(), anyBoolean());
    }

    @Test
    void getAllFormation_EmptyFormations_ThrowsException() {
        authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null);

        when(formationDAO.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class,
                () -> formationService.getAllFormation(authentication));
    }

    // getFormationsForFormateur tests


    // updateFormationImage tests
    @Test
    void updateFormationImage_ValidId_UpdatesSuccessfully() {
        when(formationDAO.findById(anyLong())).thenReturn(Optional.of(testFormation));

        formationService.updateFormationImage(1L, "new-image.jpg", "public123");

        assertEquals("new-image.jpg", testFormation.getImageUrl());
        assertEquals("public123", testFormation.getPublicId());
        verify(formationDAO).save(testFormation);
    }

    @Test
    void updateFormationImage_InvalidId_ThrowsException() {
        when(formationDAO.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> formationService.updateFormationImage(999L, "img.jpg", "pub123"));
    }

    // getPublicIdFromFormationData tests
    @Test
    void getPublicIdFromFormationData_ValidId_ReturnsPublicId() {
        testFormation.setPublicId("public123");
        when(formationDAO.findById(anyLong())).thenReturn(Optional.of(testFormation));

        String result = formationService.getPublicIdFromFormationData(1L);

        assertEquals("public123", result);
    }

    // getFormationById tests
    @Test
    void getFormationById_ValidId_ReturnsFormation() {
        when(formationDAO.findById(anyLong())).thenReturn(Optional.of(testFormation));

        FormationManagerDto result = formationService.getFormationById(1L);

        assertEquals(formationMapper.returnformationManagerDto(testFormation), result);
    }

    @Test
    void getFormationById_InvalidId_ThrowsException() {
        when(formationDAO.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> formationService.getFormationById(999L));
    }




    // getFormationByName tests
    @Test
    void getFormationByName_FormateurRole_ReturnsFormation() {




        mockUser.setRole(Role.FORMATEUR);
        authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null);

        when(formationDAO.findFormationByNameForFormateur(anyString(), anyLong()))
                .thenReturn(Optional.of(testFormation));


        when(formationMapper.returnformationDto(testFormation)).thenReturn(formationDto);
        Object result = formationService.getFormationByName("Test", authentication);

        Assertions.assertThat(result).isNotNull();

    }




    @Test
    void getFormationByName_StudentRole_ReturnsDTO() {



        authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null);

        when(formationDAO.findFormationByName(anyString())).thenReturn(Optional.of(testFormation));
        when(enrollementDAO.isEnrollmentPaid(anyLong(), anyLong())).thenReturn(true);
        when(enrollementDAO.isStudentEnrolled(anyLong(), anyLong())).thenReturn(true);
        when(formationMapper.returnformationStudentDto(any(), anyBoolean(), anyBoolean()))
                .thenReturn(new FormationStudentDto());

        Object result = formationService.getFormationByName("Test", authentication);

        assertTrue(result instanceof FormationStudentDto);



    }






    // createNewFormation tests
    @Test
    void createNewFormation_ValidFormation_ReturnsSavedFormationDto() {



        Formateur formateur=Formateur.builder()
                .id(1L)
                .username("john_doe")
                .email("john@example.com")
                .firstName("jhon")
                .lastName("oo")
                .role(Role.STUDENT)
                .password("password")
                .phoneNumber("55550")
                .availability(Availability.AVAILABLE)
                .build();

        CreateFormationDto createFormationDto = CreateFormationDto.builder()
                .formationDto(formationDto)
                .formateur_ids(Arrays.asList(formateur.getId()))
                .build();

        FormationManagerDto expectedDto = FormationManagerDto.builder()
                .formation_id(testFormation.getId())
                .formation_name(testFormation.getFormationName())
                .description(testFormation.getDescription())
                .date(testFormation.getDate())
                .imageUrl(testFormation.getImageUrl())
                .publicId(testFormation.getPublicId())
                .build();


        when(formationMapper.ToFormationForCreation(createFormationDto,Set.of(formateur))).thenReturn(testFormation);
        when(formateurDAO.findById(any(Long.class))).thenReturn(Optional.of(formateur));
        when(formationMapper.returnformationManagerDto(testFormation))
                .thenReturn(expectedDto);
        when(formationDAO.save(any(Formation.class))).thenReturn(testFormation);

        FormationManagerDto result = formationService.createNewFormation(createFormationDto);

        assertNotNull(result);
        verify(formationDAO).save(testFormation);
    }

    // rem_Formation tests
    @Test
    void rem_Formation_ValidId_DeletesFormation() {
        when(formationDAO.findById(anyLong())).thenReturn(Optional.of(testFormation));

        formationService.rem_Formation(1L);

        verify(formationDAO).deleteById(1L);
    }





    // update_formation tests
    @Test
    void update_formation_ValidFormation_ReturnsUpdated() {
        Skill skill =new Skill();
        Field field = new Field();
        // Arrange - identical to createNewFormation structure
        Formateur formateur1 = Formateur.builder()
                .skills(Set.of(skill))
                .field(field)
                .experienceYears(10)
                .availability(Availability.AVAILABLE)
                .id(1L)
                .username("formateur1")
                .build();

        Formateur formateur2 = Formateur.builder()
                .skills(Set.of(skill))
                .field(field)
                .experienceYears(2)
                .availability(Availability.AVAILABLE)
                .id(2L)
                .username("formateur2")
                .build();

        FormationDto formationDto = FormationDto.builder()
                .formation_id(testFormation.getId())
                .formation_name("Updated Name")
                .description("Updated Description")
                .date(LocalDate.now().plusDays(1))
                .imageUrl("new-image.jpg")
                .build();

        CreateFormationDto updateDto = CreateFormationDto.builder()
                .formationDto(formationDto)
                .formateur_ids(Arrays.asList(1L, 2L))
                .build();

        Formation updatedFormation = Formation.builder()
                .id(testFormation.getId())
                .formationName("Updated Name")
                .description("Updated Description")
                .date(LocalDate.now().plusDays(1))
                .imageUrl("new-image.jpg")
                .formateurs(Set.of(formateur1, formateur2))
                .build();

        FormationDto expectedDto = FormationDto.builder()
                .formation_id(updatedFormation.getId())
                .formation_name(updatedFormation.getFormationName())
                .description(updatedFormation.getDescription())
                .date(updatedFormation.getDate())
                .imageUrl(updatedFormation.getImageUrl())
                .build();

        // Mocking - identical to createNewFormation pattern
        when(formationDAO.findById(testFormation.getId())).thenReturn(Optional.of(testFormation));
        when(formateurDAO.findById(1L)).thenReturn(Optional.of(formateur1));
        when(formateurDAO.findById(2L)).thenReturn(Optional.of(formateur2));
        when(formationMapper.returnFormation(eq(updateDto), eq(testFormation)))
                .thenReturn(updatedFormation);
        when(formationDAO.save(updatedFormation)).thenReturn(updatedFormation);
        when(formationMapper.returnformationDto(updatedFormation)).thenReturn(expectedDto);

        // Act
        FormationDto result = formationService.update_formation(updateDto);

        // Assert - identical verification style
        assertNotNull(result);
        assertEquals(expectedDto.getFormation_id(), result.getFormation_id());
        assertEquals(expectedDto.getFormation_name(), result.getFormation_name());
        assertEquals(expectedDto.getDescription(), result.getDescription());
        assertEquals(expectedDto.getDate(), result.getDate());
        assertEquals(expectedDto.getImageUrl(), result.getImageUrl());

        verify(formationDAO).findById(testFormation.getId());
        verify(formateurDAO).findById(1L);
        verify(formateurDAO).findById(2L);
        verify(formationMapper).returnFormation(updateDto, testFormation);
        verify(formationDAO).save(updatedFormation);
        verify(formationMapper).returnformationDto(updatedFormation);
    }

    @Test
    void updateFormationImage_shouldUpdateImage() {

        when(formationDAO.findById(testFormation.getId())).thenReturn(Optional.of(testFormation));

        formationService.updateFormationImage(testFormation.getId(), "img-url", "public-id");

        assertEquals("img-url", testFormation.getImageUrl());
        assertEquals("public-id", testFormation.getPublicId());
        verify(formationDAO).save(testFormation);

    }
    @Test
    void updateFormationImage_shouldThrowIfNotFound() {
        when(formationDAO.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () ->
                formationService.updateFormationImage(1L, "url", "pid")
        );
    }
    @Test
    void getFormationsforEnrolledStudent_shouldReturnList() {
        authentication =
                new UsernamePasswordAuthenticationToken(mockUser, null);

        when(formationDAO.getFormationsforEnrolledStudent(mockUser.getId())).thenReturn(List.of(testFormation));
        when(enrollementDAO.isEnrollmentPaid(mockUser.getId(), testFormation.getId())).thenReturn(true);

        List<FormationStudentDto> result = formationService.getFormationsforEnrolledStudent(authentication);
        assertEquals(1, result.size());
    }
}


