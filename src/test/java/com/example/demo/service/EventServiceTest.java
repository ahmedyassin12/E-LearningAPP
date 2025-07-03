package com.example.demo.service;


import com.example.demo.Dtos.eventDto.CreateEventDto;
import com.example.demo.Dtos.eventDto.EventDto;
import com.example.demo.Dtos.eventDto.EventManagerDto;
import com.example.demo.dao.EventDAO;
import com.example.demo.dao.EventEnrollementDAO;
import com.example.demo.dao.FormateurDAO;
import com.example.demo.entity.*;
import com.example.demo.entity.Enums.Role;
import com.example.demo.mapper.EventMapper;
import com.example.demo.validator.ObjectValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventDAO eventDAO;

    @Mock
    private EventEnrollementDAO eventEnrollementDAO;

    @Mock
    private ObjectValidator<CreateEventDto> eventValidator ;

    @Mock
    private FormateurDAO formateurDAO;

    @Spy
    private EventMapper eventMapper = new EventMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private Principal getMockPrincipalWithRole(Role role) {
        User user = new User();
        user.setRole(role);
        user.setId(1L);
        return new UsernamePasswordAuthenticationToken(user, null);
    }

    @Test
    void testGetAllEvents_AsManager_ReturnsManagerDtos() {

        Principal principal = getMockPrincipalWithRole(Role.MANAGER);

        Event event = Event.builder().event_id(1L).build();
        List<Event> eventList = List.of(event);
        when(eventDAO.findAll()).thenReturn(eventList);

        Object result = eventService.getAllEvents(principal);
        assertNotNull(result);
        assertTrue(result instanceof List<?>);



    }

    @Test
    void testGetEventById_ReturnsDto() {
        Event event = Event.builder().event_id(1L).build();
        when(eventDAO.findById(1L)).thenReturn(Optional.of(event));

        EventManagerDto dto = eventService.getEventById(1L);

        assertNotNull(dto);
    }

    @Test
    void testCreateNewEvent_Success() {
        CreateEventDto createDto = new CreateEventDto();
        createDto.setFormateur_ids(List.of(1L));
        createDto.setEventDto(new EventDto());

        Formateur formateur = Formateur.builder().id(1L).build();
        when(formateurDAO.findById(1L)).thenReturn(Optional.of(formateur));

        EventManagerDto result = eventService.createNewEvent(createDto);

        assertNotNull(result);
        verify(eventDAO).save(any(Event.class));
    }

    @Test
    void testUpdateEvent_Success() {
        CreateEventDto createDto = new CreateEventDto();
        EventDto dto = new EventDto();
        dto.setEventId(1L);
        createDto.setEventDto(dto);
        createDto.setFormateur_ids(List.of(1L));

        Event existing = Event.builder().event_id(1L).build();
        Formateur formateur = Formateur.builder().id(1L).build();

        when(eventDAO.findById(1L)).thenReturn(Optional.of(existing));
        when(formateurDAO.findById(1L)).thenReturn(Optional.of(formateur));

        EventDto result = eventService.update_event(createDto);

        assertNotNull(result);
        verify(eventDAO).save(any(Event.class));


    }

    @Test
    void testDeleteEvent_Success() {
        Event event = Event.builder().event_id(1L).build();
        when(eventDAO.findById(1L)).thenReturn(Optional.of(event));

        String message = eventService.rem_event(1L);
        assertEquals("Event deleted successfully", message);
        verify(eventDAO).deleteById(1L);
    }

    @Test
    void testGetPublicIdFromEventData_ReturnsPublicId() {
        Event event = Event.builder().event_id(1L).publicId("public-id-123").build();
        when(eventDAO.findById(1L)).thenReturn(Optional.of(event));

        String result = eventService.getPublicIdFromEventData(1L);

        assertEquals("public-id-123", result);
    }

    @Test
    void testGetPublicIdFromEventData_ThrowsIfNull() {
        Event event = Event.builder().event_id(1L).publicId(null).build();
        when(eventDAO.findById(1L)).thenReturn(Optional.of(event));

        assertThrows(NullPointerException.class, () -> eventService.getPublicIdFromEventData(1L));
    }
}

