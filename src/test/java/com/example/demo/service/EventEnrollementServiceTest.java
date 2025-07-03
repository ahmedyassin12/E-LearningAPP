package com.example.demo.service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.Dtos.EventEnrollementDto.EventEnrollementDto;
import com.example.demo.dao.EventDAO;
import com.example.demo.dao.EventEnrollementDAO;
import com.example.demo.dao.StudentDAO;
import com.example.demo.entity.*;
import com.example.demo.mapper.EventEnrollementMapper;
import com.example.demo.validator.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EventEnrollementServiceTest {

    @Mock private EventEnrollementDAO enrollementDAO;
    @Mock private EventDAO eventDAO;
    @Mock private StudentDAO studentDAO;
    @Mock
    private ObjectValidator<EventEnrollementDto> eventValidator ;

    @InjectMocks
    private EventEnrollementService service;

    private final EventEnrollementMapper mapper = new EventEnrollementMapper();

    // Test Entities
    private Student createStudent() {
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        return student;
    }

    private Event createEvent() {
        Event event = new Event();
        event.setEvent_id(1L);
        event.setEventName("Tech Conference");
        return event;
    }

    private EventEnrollement createEnrollement() {
        EventEnrollement enrollement = new EventEnrollement();
        enrollement.setId(1L);
        enrollement.setStudent(createStudent());
        enrollement.setEvent(createEvent());
        enrollement.setRating(5);
        enrollement.setEnrollementDate(LocalDate.now());
        return enrollement;
    }

    // Tests for getAllEnrollements
    @Test
    void getAllEnrollements_ReturnsList() {
        EventEnrollement enrollement = createEnrollement();
        when(enrollementDAO.findAll()).thenReturn(Collections.singletonList(enrollement));

        List<EventEnrollementDto> result = service.getAllEnrollements();

        assertEquals(1, result.size());
        assertEquals(enrollement.getId(), result.get(0).getId());
    }

    @Test
    void getAllEnrollements_EmptyList_ThrowsException() {
        when(enrollementDAO.findAll()).thenReturn(Collections.emptyList());
        assertThrows(EntityNotFoundException.class, () -> service.getAllEnrollements());
    }

    // Tests for getEventEnrollementForStudent (ID version)
    @Test
    void getEventEnrollementForStudentId_ReturnsList() {
        Long studentId = 1L;
        when(enrollementDAO.getEventEnrollementForStudent(studentId))
                .thenReturn(Collections.singletonList(createEnrollement()));

        List<EventEnrollementDto> result = service.getEventEnrollementForStudent(studentId);
        assertEquals(1, result.size());
    }

    @Test
    void getEventEnrollementForStudentId_Empty_ThrowsException() {
        Long studentId = 99L;
        when(enrollementDAO.getEventEnrollementForStudent(studentId)).thenReturn(Collections.emptyList());
        assertThrows(EntityNotFoundException.class, () -> service.getEventEnrollementForStudent(studentId));
    }

    // Tests for getEventEnrollementForStudent (Principal version)
    @Test
    void getEventEnrollementForStudentPrincipal_ReturnsList() {
        User user = new User();
        user.setId(1L);
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);

        when(enrollementDAO.getEventEnrollementForStudent(user.getId()))
                .thenReturn(Collections.singletonList(createEnrollement()));

        List<EventEnrollementDto> result = service.getEventEnrollementForStudent(principal);
        assertEquals(1, result.size());
    }

    // Tests for getEventEnrollement
    @Test
    void getEventEnrollement_ValidEventId_ReturnsList() {
        Long eventId = 1L;
        when(enrollementDAO.getEventEnrollement(eventId))
                .thenReturn(Collections.singletonList(createEnrollement()));

        List<EventEnrollementDto> result = service.getEventEnrollement(eventId);
        assertEquals(1, result.size());
    }

    @Test
    void getEventEnrollement_InvalidEventId_ThrowsException() {
        Long eventId = 99L;
        when(enrollementDAO.getEventEnrollement(eventId)).thenReturn(Collections.emptyList());
        assertThrows(EntityNotFoundException.class, () -> service.getEventEnrollement(eventId));
    }

    // Tests for getEnrollementById
    @Test
    void getEnrollementById_ValidId_ReturnsDto() {
        Long id = 1L;
        when(enrollementDAO.findById(id)).thenReturn(Optional.of(createEnrollement()));
        EventEnrollementDto result = service.getEnrollementById(id);
        assertEquals(id, result.getId());
    }

    @Test
    void getEnrollementById_InvalidId_ThrowsException() {
        Long id = 99L;
        when(enrollementDAO.findById(id)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getEnrollementById(id));
    }

    // Tests for createNewEnrollement
    @Test
    void createNewEnrollement_ValidData_ReturnsDto() {
        EventEnrollementDto dto = new EventEnrollementDto();
        dto.setEventName("Tech Conference");
        dto.setStudentID(1L);
        dto.setRating(5);

        when(eventDAO.getEventByEventName("Tech Conference")).thenReturn(Optional.of(createEvent()));
        when(studentDAO.findById(1L)).thenReturn(Optional.of(createStudent()));
        when(enrollementDAO.save(any())).thenAnswer(inv -> inv.getArgument(0));

        EventEnrollementDto result = service.createNewEnrollement(dto);
        assertNotNull(result);
        verify(enrollementDAO).save(any());

    }

    @Test
    void createNewEnrollement_InvalidEventName_ThrowsException() {
        EventEnrollementDto dto = new EventEnrollementDto();
        dto.setEventName("Unknown Event");
        when(eventDAO.getEventByEventName("Unknown Event")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.createNewEnrollement(dto));
    }

    // Tests for removeEnrollement
    @Test
    void removeEnrollement_ValidId_ReturnsSuccess() {
        Long id = 1L;
        EventEnrollement eventEnrollement = new EventEnrollement();
eventEnrollement.setId(id);
        when(enrollementDAO.findById(id)).thenReturn(Optional.of(eventEnrollement));

        String result = service.removeEnrollement(id);
        assertEquals("Enrollement removed successfully", result);
        verify(enrollementDAO).deleteById(id);
    }

    @Test
    void removeEnrollement_InvalidId_ThrowsException() {

        when(enrollementDAO.findById(99l)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.removeEnrollement(99l));
    }

    // Tests for updateEnrollement
    @Test
    void updateEnrollement_ValidData_ReturnsDto() {
        EventEnrollementDto dto = new EventEnrollementDto();
        dto.setId(1L);
        dto.setEventName("Tech Conference");
        dto.setStudentID(1L);
        dto.setRating(4);

        EventEnrollement existing = createEnrollement();
        when(enrollementDAO.findById(1L)).thenReturn(Optional.of(existing));
        when(eventDAO.getEventByEventName("Tech Conference")).thenReturn(Optional.of(createEvent()));
        when(studentDAO.findById(1L)).thenReturn(Optional.of(createStudent()));

        EventEnrollementDto result = service.updateEnrollement(dto);
        assertEquals(dto.getRating(), result.getRating());
        verify(enrollementDAO).save(existing);
    }

    @Test
    void updateEnrollement_InvalidEnrollementId_ThrowsException() {
        EventEnrollementDto dto = new EventEnrollementDto();
        dto.setId(99L);
        when(enrollementDAO.findById(99L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.updateEnrollement(dto));
    }
}
