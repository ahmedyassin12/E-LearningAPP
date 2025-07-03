package com.example.demo.service;

import com.example.demo.Dtos.eventDto.CreateEventDto;
import com.example.demo.Dtos.eventDto.EventDto;
import com.example.demo.Dtos.eventDto.EventManagerDto;
import com.example.demo.Dtos.eventDto.EventStudentDto;
import com.example.demo.dao.EventDAO;
import com.example.demo.dao.EventEnrollementDAO;
import com.example.demo.dao.FormateurDAO;
import com.example.demo.entity.*;
import com.example.demo.entity.Enums.Role;
import com.example.demo.mapper.EventMapper;
import com.example.demo.validator.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
@Slf4j
public class EventService {

    @Autowired
    private EventDAO eventDAO;


    @Autowired
    private EventEnrollementDAO eventEnrollementDAO;

    @Autowired
    private FormateurDAO formateurDAO;

    @Autowired
    private ObjectValidator<CreateEventDto> eventValidator ;
    private EventMapper eventMapper=new EventMapper();

    public Iterable<?> getAllEvents(Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Iterable<Event> events = eventDAO.findAll();

        if (events.iterator().hasNext()) {
            if (user.getRole() == Role.MANAGER) {
                List<EventManagerDto> managerDtos = new ArrayList<>();
                events.forEach(event -> managerDtos.add(eventMapper.returnEventManagerDto(event)));
                return managerDtos;
            } else if (user.getRole() == Role.STUDENT) {
                List<EventStudentDto> studentDtos = new ArrayList<>();
                for (Event event : events) {
                    boolean isEnrolled = eventEnrollementDAO.isStudentEnrolled(user.getId(), event.getEvent_id());
                    studentDtos.add(eventMapper.returnEventStudentDto(event, isEnrolled));
                }
                return studentDtos;
            }
        }
        throw new EntityNotFoundException("Events are empty");
    }

    //formateur
    public List<EventDto> getEventsForFormateur(Principal connectedUser ) {


        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();


        if(!user.getRole().equals(Role.FORMATEUR)) throw new IllegalArgumentException("User must be formateur !!");

        Iterable< Event >  events=eventDAO.getEventsForFormateur(user.getId());

        if(events.iterator().hasNext()){
            List<EventDto>eventDtos=new ArrayList<>();

            events.forEach(event ->
                    eventDtos.add(
                            eventMapper.returnEventDto(event)
                    ) );

            return eventDtos ;

        }

        throw new EntityNotFoundException("Events are empty");

    }







    // Manager
    public EventManagerDto getEventById(Long id) {
        Event event = eventDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found for id :: " + id));
        return eventMapper.returnEventManagerDto(event);
    }

    public Object getEventByStreetLocation(String eventLocation, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Event event = eventDAO.getEventByStreetLocation(eventLocation)
                .orElseThrow(() -> new EntityNotFoundException("Event Not found for street location "+eventLocation));

        if (user.getRole() == Role.MANAGER) {
            return eventMapper.returnEventManagerDto(event);
        } else if (user.getRole() == Role.STUDENT) {
            boolean isEnrolled = eventEnrollementDAO.isStudentEnrolled(user.getId(), event.getEvent_id());
            return eventMapper.returnEventStudentDto(event, isEnrolled);
        }
        else if (user.getRole() == Role.FORMATEUR) {
            Event eventFormateur = eventDAO.findEventByNameForFormateur(eventLocation, user.getId())
                    .orElseThrow( () -> new AccessDeniedException(
                            "Formateur not associated with formation: " +eventLocation
                    ));
            return eventMapper.returnEventDto(event) ;


        }
        throw new AccessDeniedException("Unauthorized role: " + user.getRole());
    }

    public Object getEventByName(String eventName, Principal connectedUser) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        Event event = eventDAO.getEventByEventName(eventName)
                .orElseThrow(() -> new EntityNotFoundException(eventName));

        if (user.getRole() == Role.MANAGER) {
            return eventMapper.returnEventManagerDto(event);
        } else if (user.getRole() == Role.STUDENT) {
            boolean isEnrolled = eventEnrollementDAO.isStudentEnrolled(user.getId(), event.getEvent_id());
            return eventMapper.returnEventStudentDto(event, isEnrolled);
        }
        else if (user.getRole() == Role.FORMATEUR) {
            Event eventFormateur = eventDAO.findEventByNameForFormateur(eventName, user.getId())
                    .orElseThrow( () -> new AccessDeniedException(
                            "Formateur not associated with formation: " +eventName
                    ));
            return eventMapper.returnEventDto(event) ;


        }
        throw new AccessDeniedException("Unauthorized role: " + user.getRole());
    }


    public EventManagerDto  createNewEvent(CreateEventDto createEventDto) {


        eventValidator.validate(createEventDto);

        Set<Formateur> formateurs = new HashSet<>() ;
            Optional <Formateur> formateur ;
            for(Long i : createEventDto.getFormateur_ids()){


                formateur=formateurDAO.findById(i) ;

                if(formateur.isPresent()){
                    formateurs.add( formateur.get()  ) ;
                }




            }

            Event event =eventMapper.ToEventForCreation(createEventDto,formateurs);

            eventDAO.save(event);

            return eventMapper.returnEventManagerDto(event);

        }




    public String  rem_event(Long id) {

       Event event= eventDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));



        eventDAO.deleteById(id);

        return "Event deleted successfully";

    }

    //manager
    public EventDto update_event(CreateEventDto createEventDto) {

        eventValidator.validate(createEventDto);

        Optional<Event> event = eventDAO.findById(createEventDto.getEventDto().getEventId());


        if (event.isPresent()) {
            Set<Formateur> formateurs = new HashSet<>();
            Optional<Formateur> formateur;
            for (Long i : createEventDto.getFormateur_ids()) {


                formateur = formateurDAO.findById(i);
                if (formateur.isPresent()) {
                    formateurs.add(formateur.get());
                }


            }

            event.get().setFormateurs(formateurs);

            Event updated_event = eventMapper.returnEvent(createEventDto, event.get());


            eventDAO.save(updated_event);

            return eventMapper.returnEventDto(updated_event);


        }

        throw new EntityNotFoundException("cant find Event to update !! ");
    }



    public void updateEventImage(Long eventId, String url, String publicId) {

        Event event =eventDAO.findById(eventId).orElseThrow(()->new EntityNotFoundException("event not found "));

        event.setImageUrl(url);
        event.setPublicId(publicId);
        eventDAO.save(event);


    }

    //manager
    public String  getPublicIdFromEventData(Long id ){



        Event event= eventDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found "));

        String public_id ;


        public_id = event.getPublicId() ;

        if(public_id!=null) return public_id ;

        throw new NullPointerException("public_id is null ") ;





    }


}