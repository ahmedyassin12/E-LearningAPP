package com.example.demo.validator;


import com.example.demo.Dtos.courseDto.CreateCourseDto;
import com.example.demo.Dtos.courseDto.InitiateCourseDto;
import com.example.demo.Dtos.enrollementDto.EnrollementDto;
import com.example.demo.Dtos.eventDto.CreateEventDto;
import com.example.demo.Dtos.eventDto.EventDto;
import com.example.demo.Dtos.formationDto.CreateFormationDto;
import com.example.demo.Dtos.formationDto.FormationDto;
import com.example.demo.Dtos.paymentDto.CreatePaymentDto;
import com.example.demo.Dtos.paymentDto.PaymentDto;
import com.example.demo.Dtos.userDto.CreateFormateurDto;
import com.example.demo.auth.Dto.RegisterRequest;
import com.example.demo.entity.Enums.Availability;
import com.example.demo.entity.Enums.PaymentStatus;
import com.example.demo.exceptions.ObjectNotValidException;
import jakarta.validation.UnexpectedTypeException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class EnrollementDtoValidatorTest {


    private final ObjectValidator <EnrollementDto> enrollementValidator=new ObjectValidator<>();

    private final ObjectValidator <CreateCourseDto> courseValidator=new ObjectValidator<>();
    private final ObjectValidator <CreateEventDto> eventValidator=new ObjectValidator<>();
    private final ObjectValidator <CreateFormationDto> formationValidator=new ObjectValidator<>();

    private final ObjectValidator <CreatePaymentDto> paymentValidator =new ObjectValidator<>();

    private final ObjectValidator <CreateFormateurDto> formateurValidator =new ObjectValidator<>();

    private final ObjectValidator <RegisterRequest> registerValidator =new ObjectValidator<>();



    @Test
    void validate_ValidDto_ShouldPass() {
        EnrollementDto dto = EnrollementDto.builder()
                .enrollementId(1L)
                .paymentStatus(PaymentStatus.Paid)
                .rating(4)
                .enrollementDate(LocalDate.of(2024, 6, 10))
                .studentId(10L)
                .formationName("Java Basics")
                .build();

        assertDoesNotThrow(() -> enrollementValidator.validate(dto));
    }

    @Test
    void validate_MissingStudentId_ShouldThrow() {
        EnrollementDto dto = EnrollementDto.builder()
                .paymentStatus(PaymentStatus.Paid)
                .rating(4)
                .enrollementDate(LocalDate.of(2024, 6, 10))
                .formationName("Java Basics")
                .build(); // studentId is null

         assertThrows(ObjectNotValidException.class,
                () -> enrollementValidator.validate(dto));


    }

    @Test
    void validate_EmptyFormationName_ShouldThrow() {
        EnrollementDto dto = EnrollementDto.builder()
                .paymentStatus(PaymentStatus.Paid)
                .rating(4)
                .enrollementDate(LocalDate.of(2024, 6, 10))
                .studentId(20L)
                .formationName("")
                .build(); // formationName is empty

        assertThrows(ObjectNotValidException.class,
                () -> enrollementValidator.validate(dto));

    }



    // -------- Tests for CreateCourseDto --------

    @Test
    void validCreateCourseDto_shouldPassValidation() {
        InitiateCourseDto initDto = InitiateCourseDto.builder()
                .courseName("Spring Boot")
                .course_description("Advanced Spring")
                .build();

        CreateCourseDto dto = CreateCourseDto.builder()
                .initiateCourseDto(initDto)
                .formation_id(100L)
                .publicId("public-id")
                .build();

        assertDoesNotThrow(
                () ->        courseValidator.validate(dto)  );

    }
    @Test
    void missingCourseName_shouldNotPassValidation() {
        InitiateCourseDto initDto = InitiateCourseDto.builder()
                .course_description("Advanced Spring")
                .build();

        CreateCourseDto dto = CreateCourseDto.builder()
                .initiateCourseDto(initDto)
                .formation_id(100L)
                .publicId("public-id")
                .build();

        assertThrows(ObjectNotValidException.class,
                () ->        courseValidator.validate(dto)  );

    }

    @Test
    void missingCourseDescription_shouldNotPassValidation() {
        InitiateCourseDto initDto = InitiateCourseDto.builder()
                .courseName("Spring Boot")
                .build();

        CreateCourseDto dto = CreateCourseDto.builder()
                .initiateCourseDto(initDto)
                .formation_id(100L)
                .publicId("public-id")
                .build();

        assertThrows(ObjectNotValidException.class,
                () ->        courseValidator.validate(dto)  );

    }

    @Test
    void missingFormationId_shouldNotPassValidation() {
        InitiateCourseDto initDto = InitiateCourseDto.builder()
                .courseName("Spring Boot")
                .course_description("advanced spring book tut")
                .build();

        CreateCourseDto dto = CreateCourseDto.builder()
                .initiateCourseDto(initDto)
                .publicId("public-id")
                .build();

        assertThrows(ObjectNotValidException.class,
                () ->        courseValidator.validate(dto)  );

    }
    @Test
    void invalidCreateCourseDto_shouldFailValidation() {
        CreateCourseDto dto = new CreateCourseDto(); // completely empty
        assertThrows( ObjectNotValidException.class,
                () ->        courseValidator.validate(dto)  );

    }

    // -------- Tests for CreateEventDto --------
    @Test
    void validate_invalidCreateEventDto_shouldThrowException() {
        // invalid EventDto: empty fields
        EventDto eventDto = EventDto.builder()
                .EventName("") // Invalid: @NotEmpty
                .date(null) // Invalid: @NotNull
                .description("") // Invalid
                .cityLocation("") // Invalid
                .streetLocation("") // Invalid
                .build();

        // CreateEventDto with empty formateur list and invalid nested EventDto
        CreateEventDto createEventDto = CreateEventDto.builder()
                .eventDto(eventDto)
                .formateur_ids(Collections.emptyList()) // Invalid: @NotEmpty
                .build();

        assertThrows(ObjectNotValidException.class, () -> eventValidator.validate(createEventDto));
    }

    @Test
    void validate_validCreateEventDto_shouldNotThrowException() {
        EventDto eventDto = EventDto.builder()
                .EventName("Spring Boot Workshop")
                .date(LocalDate.now())
                .description("Full-day workshop")
                .cityLocation("Tunis")
                .streetLocation("Rue de Dev")
                .build();

        CreateEventDto createEventDto = CreateEventDto.builder()
                .eventDto(eventDto)
                .formateur_ids(Collections.singletonList(1L))
                .build();

        // No exception expected
        eventValidator.validate(createEventDto);
    }
    @Test
    void validate_MissingEventName_shouldThrowException() {
        EventDto eventDto = EventDto.builder()
                .date(LocalDate.now())
                .description("Full-day workshop")
                .cityLocation("Tunis")
                .streetLocation("Rue de Dev")
                .build();

        CreateEventDto createEventDto = CreateEventDto.builder()
                .eventDto(eventDto)
                .formateur_ids(Collections.singletonList(1L))
                .build();

        // No exception expected
        assertThrows(ObjectNotValidException.class, () -> eventValidator.validate(createEventDto));

    }

    @Test
    void validate_MissingEventDate_shouldThrowException() {
        EventDto eventDto = EventDto.builder()
                .description("Full-day workshop")
                .cityLocation("Tunis")
                .streetLocation("Rue de Dev")
                .EventName("hohoho")
                .build();

        CreateEventDto createEventDto = CreateEventDto.builder()
                .eventDto(eventDto)
                .formateur_ids(Collections.singletonList(1L))
                .build();

        // No exception expected
        assertThrows(ObjectNotValidException.class, () -> eventValidator.validate(createEventDto));

    }
    @Test
    void validate_MissingCityLocation_shouldThrowException() {
        EventDto eventDto = EventDto.builder()
                .date(LocalDate.now())
                .description("Full-day workshop")
                .EventName("hdsl")
                .streetLocation("Rue de Dev")
                .build();

        CreateEventDto createEventDto = CreateEventDto.builder()
                .eventDto(eventDto)
                .formateur_ids(Collections.singletonList(1L))
                .build();

        // No exception expected
        assertThrows(ObjectNotValidException.class, () -> eventValidator.validate(createEventDto));

    }
    @Test
    void validate_MissingstreetLocation_shouldThrowException() {
        EventDto eventDto = EventDto.builder()
                .date(LocalDate.now())
                .description("Full-day workshop")
                .cityLocation("Tunis")
                .EventName("hohoho")
                .build();

        CreateEventDto createEventDto = CreateEventDto.builder()
                .eventDto(eventDto)
                .formateur_ids(Collections.singletonList(1L))
                .build();

        // No exception expected
        assertThrows(ObjectNotValidException.class, () -> eventValidator.validate(createEventDto));

    }
    @Test
    void validate_MissingformateurIds_shouldThrowException() {
        EventDto eventDto = EventDto.builder()
                .date(LocalDate.now())
                .description("Full-day workshop")
                .cityLocation("Tunis")
                .EventName("ohohoh")
                .streetLocation("Rue de Dev")
                .build();

        CreateEventDto createEventDto = CreateEventDto.builder()
                .eventDto(eventDto)
                .build();

        // No exception expected
        assertThrows(ObjectNotValidException.class, () -> eventValidator.validate(createEventDto));

    }

//Formation Validator Test
    @Test
    void validate_invalidCreateFormationDto_shouldThrowException() {
        FormationDto formationDto = FormationDto.builder()
                .formation_name("")           // Invalid: @NotEmpty
                .description("")              // Invalid: @NotEmpty
                .date(null)                   // Invalid: @NotNull
                .build();

        CreateFormationDto dto = CreateFormationDto.builder()
                .formationDto(formationDto)
                .formateur_ids(Collections.emptyList()) // Invalid: @NotNull but also logically invalid as empty
                .build();

        assertThrows(ObjectNotValidException.class, () -> formationValidator.validate(dto));
    }

    @Test
    void validate_validCreateFormationDto_shouldNotThrowException() {
        FormationDto formationDto = FormationDto.builder()
                .formation_name("Spring Boot Advanced")
                .description("Deep dive into Spring Boot")
                .date(LocalDate.now())
                .build();

        CreateFormationDto dto = CreateFormationDto.builder()
                .formationDto(formationDto)
                .formateur_ids(List.of(1L, 2L))
                .build();

        assertDoesNotThrow(() -> formationValidator.validate(dto) ); // should not throw
    }
    @Test
    void shouldThrow_whenFormationNameIsEmpty() {
        FormationDto formationDto = FormationDto.builder()
                .formation_name("") // @NotEmpty violation
                .description("Valid description")
                .date(LocalDate.now())
                .build();

        CreateFormationDto dto = CreateFormationDto.builder()
                .formationDto(formationDto)
                .formateur_ids(List.of(1L))
                .build();

        assertThrows(ObjectNotValidException.class, () -> formationValidator.validate(dto));
    }

    // 2. description is empty
    @Test
    void shouldThrow_whenDescriptionIsEmpty() {
        FormationDto formationDto = FormationDto.builder()
                .formation_name("Spring Boot")
                .description("") // @NotEmpty violation
                .date(LocalDate.now())
                .build();

        CreateFormationDto dto = CreateFormationDto.builder()
                .formationDto(formationDto)
                .formateur_ids(List.of(1L))
                .build();

        assertThrows(ObjectNotValidException.class, () -> formationValidator.validate(dto));
    }

    // 3. date is null
    @Test
    void shouldThrow_whenDateIsNull() {
        FormationDto formationDto = FormationDto.builder()
                .formation_name("Spring Boot")
                .description("Valid description")
                .date(null) // @NotNull violation
                .build();

        CreateFormationDto dto = CreateFormationDto.builder()
                .formationDto(formationDto)
                .formateur_ids(List.of(1L))
                .build();

        assertThrows(ObjectNotValidException.class, () -> formationValidator.validate(dto));
    }

    // 4. formateur_ids is null
    @Test
    void shouldThrow_whenFormateurIdsIsNull() {
        FormationDto formationDto = FormationDto.builder()
                .formation_name("Spring Boot")
                .description("Valid description")
                .date(LocalDate.now())
                .build();

        CreateFormationDto dto = CreateFormationDto.builder()
                .formationDto(formationDto)
                .formateur_ids(null) // @NotNull violation
                .build();

        assertThrows(ObjectNotValidException.class, () -> formationValidator.validate(dto));
    }

    //paymment validator :
    // 1. amount is null
    @Test
    void shouldThrow_whenAmountIsNull() {
        PaymentDto paymentDto = PaymentDto.builder()
                .paymentDate(LocalDate.now())
                .build();

        CreatePaymentDto dto = CreatePaymentDto.builder()
                .paymentDto(paymentDto) // This field is @Valid but not @NotNull, so it's valid
                .enrollement_id(1L)
                .build();

        assertThrows(ObjectNotValidException.class, () -> paymentValidator.validate(dto));
    }

    // 2. paymentDate is null
    @Test
    void shouldThrow_whenPaymentDateIsNull() {
        PaymentDto paymentDto = PaymentDto.builder()
                .amount(100.0)
                .paymentDate(null) // @NotNull violation
                .build();

        CreatePaymentDto dto = CreatePaymentDto.builder()
                .paymentDto(paymentDto)
                .enrollement_id(1L)
                .build();

        assertThrows(ObjectNotValidException.class, () -> paymentValidator.validate(dto));
    }

    // 3. enrollement_id is null
    @Test
    void shouldThrow_whenEnrollementIdIsNull() {
        PaymentDto paymentDto = PaymentDto.builder()
                .amount(100.0)
                .paymentDate(LocalDate.now())
                .build();

        CreatePaymentDto dto = CreatePaymentDto.builder()
                .paymentDto(paymentDto)
                .enrollement_id(null) // @NotNull violation
                .build();

        assertThrows(ObjectNotValidException.class, () -> paymentValidator.validate(dto));
    }

    // ✅ Valid case
    @Test
    void shouldNotThrow_whenAllFieldsValid() {
        PaymentDto paymentDto = PaymentDto.builder()
                .amount(200.0)
                .paymentDate(LocalDate.now())
                .build();

        CreatePaymentDto dto = CreatePaymentDto.builder()
                .paymentDto(paymentDto)
                .enrollement_id(10L)
                .build();

assertDoesNotThrow(()->paymentValidator.validate(dto));
    }

    //User Validator Tests :

        //formateur :
        @Test
        void shouldThrow_whenSkillsIsEmpty() {
            CreateFormateurDto dto = validDto();
            dto.setSkillNames(Set.of()); // ⛔ @NotEmpty violated

            assertThrows(ObjectNotValidException.class, () -> formateurValidator.validate(dto));
        }

    @Test
    void shouldThrow_whenFieldIsEmpty() {
        CreateFormateurDto dto = validDto();
        dto.setFieldName(""); // ⛔ @NotEmpty violated

        assertThrows(ObjectNotValidException.class, () -> formateurValidator.validate(dto));
    }

    @Test
    void shouldThrow_whenExperienceTimeIsNull() {
        CreateFormateurDto dto = validDto();
        dto.setExperience_time(null); // ⛔ @NotNull violated

        assertThrows(ObjectNotValidException.class, () -> formateurValidator.validate(dto));
    }

    @Test
    void shouldThrow_whenAvailabilityIsEmpty() {
        CreateFormateurDto dto = validDto();
        //dto.setAvailability(); // ⛔ @NotEmpty violated

        assertThrows(ObjectNotValidException.class, () -> formateurValidator.validate(dto));
    }

    @Test
    void shouldThrow_whenPasswordIsWeak() {
        CreateFormateurDto dto = validDto();
        dto.setPassword("123"); // ⛔ @StrongPassword violated (assuming weak)

        assertThrows(ObjectNotValidException.class, () -> formateurValidator.validate(dto));
    }

    private CreateFormateurDto validDto() {
        return CreateFormateurDto.builder()
                .SkillNames(Set.of("Java, Spring") )
                .fieldName("Software Engineering")
                .experience_time(3)
                .availability(Availability.AVAILABLE)
                .password("StrongPass123!") // Assuming valid password
                .build();
    }



    // Register validator test :
    private RegisterRequest validRequest() {
        return RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .username("johndoe")
                .password("Strong123!")
                .phoneNumber("55999999") // valid 8-digit for Tunisia
                .dateNaissance(LocalDate.of(2000, 1, 1))
                .build();
    }

    @Test
    void shouldPassValidation_whenValid() {

        assertDoesNotThrow(() -> registerValidator.validate(validRequest()));
    }

    @Test
    void shouldThrow_whenFirstNameEmpty() {
        RegisterRequest invalid = validRequest();
        invalid.setFirstName("");
        assertThrows(ObjectNotValidException.class, () -> registerValidator.validate(invalid));
    }

    @Test
    void shouldThrow_whenLastNameEmpty() {
        RegisterRequest invalid = validRequest();
        invalid.setLastName("");
        assertThrows(ObjectNotValidException.class, () -> registerValidator.validate(invalid));
    }

    @Test
    void shouldThrow_whenEmailInvalid() {
        RegisterRequest invalid = validRequest();
        invalid.setEmail("not-an-email");
        assertThrows(ObjectNotValidException.class, () -> registerValidator.validate(invalid));
    }

    @Test
    void shouldThrow_whenUsernameInvalid() {
        RegisterRequest invalid = validRequest();
        invalid.setUsername("ab"); // Assuming @UsernameValidator has min length 3
        assertThrows(ObjectNotValidException.class, () -> registerValidator.validate(invalid));
    }

    @Test
    void shouldThrow_whenPasswordWeak() {
        RegisterRequest invalid = validRequest();
        invalid.setPassword("123"); // Should fail @StrongPassword
        assertThrows(ObjectNotValidException.class, () -> registerValidator.validate(invalid));
    }

    @Test
    void shouldThrow_whenPhoneNumberInvalid() {
        RegisterRequest invalid = validRequest();
        invalid.setPhoneNumber("123"); // Not 8 digits, invalid
        assertThrows(ObjectNotValidException.class, () -> registerValidator.validate(invalid));
    }
}
