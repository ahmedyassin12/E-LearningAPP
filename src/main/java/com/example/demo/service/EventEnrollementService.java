package com.example.demo.service;

import com.example.demo.Dtos.EventEnrollementDto.EventEnrollementDto;
import com.example.demo.dao.EventDAO;
import com.example.demo.dao.EventEnrollementDAO;
import com.example.demo.dao.StudentDAO;
import com.example.demo.entity.*;
import com.example.demo.entity.Enums.Role;
import com.example.demo.mapper.EventEnrollementMapper;
import com.example.demo.validator.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EventEnrollementService {


        @Autowired
        private EventEnrollementDAO enrollementDAO;

    @Autowired
    private ObjectValidator<EventEnrollementDto> eventEnrollementValidator;

    private EventEnrollementMapper eventEnrollementMapper=new EventEnrollementMapper();
    @Autowired
    private EventDAO eventDAO;
    @Autowired
    private StudentDAO studentDAO;


    // Manager
    public List<EventEnrollementDto> getAllEnrollements() {
        Iterable<EventEnrollement> enrollements = enrollementDAO.findAll();

        if (!enrollements.iterator().hasNext()) {
            throw new EntityNotFoundException("No event enrollements found.");
        }

        List<EventEnrollementDto> dtos = new ArrayList<>();
        enrollements.forEach(e -> dtos.add(eventEnrollementMapper.returnDto(e)));
        return dtos;
    }



    //Manager
    public List<EventEnrollementDto> getEventEnrollementForStudent(Long student_id ) {


        Iterable<EventEnrollement > enrollements=enrollementDAO.getEventEnrollementForStudent(student_id) ;


        if(enrollements.iterator().hasNext()){
            List<EventEnrollementDto> enrollementDtos=new ArrayList<>();

            enrollements.forEach(enrollement ->
                    enrollementDtos.add(
                            eventEnrollementMapper.returnDto(enrollement)
                    ) );


            return enrollementDtos ;

        }


        throw new EntityNotFoundException("No Enrollements found") ;

    }

    //Student
    public List<EventEnrollementDto> getEventEnrollementForStudent(Principal connectedUser ) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal() ;

        Iterable<EventEnrollement> enrollements=enrollementDAO.getEventEnrollementForStudent(user.getId()) ;


        if(enrollements.iterator().hasNext()){
            List<EventEnrollementDto> enrollementDtos=new ArrayList<>();

            enrollements.forEach(enrollement ->
                    enrollementDtos.add(
                            eventEnrollementMapper.returnDto(enrollement)
                    ) );


            return enrollementDtos ;

        }


        throw new EntityNotFoundException("No Enrollements found") ;

    }


    public    List<EventEnrollementDto> getEventEnrollement(Long event_id){

        Iterable<EventEnrollement> enrollements=enrollementDAO.getEventEnrollement(event_id);


        if (enrollements.iterator().hasNext()){

            List<EventEnrollementDto> enrollementDtos=new ArrayList<>();

            enrollements.forEach(enrollement ->
                    enrollementDtos.add(
                            eventEnrollementMapper.returnDto(enrollement)
                    ) );


            return enrollementDtos ;

        }


        throw new EntityNotFoundException("No Enrollements found") ;

    }

    public EventEnrollementDto getEnrollementById(Long id) {

            Optional<EventEnrollement> optional = enrollementDAO.findById(id);

            if (optional.isPresent()) {
                EventEnrollementDto enrollementDto= eventEnrollementMapper.returnDto(optional.get()) ;

                return enrollementDto;

            }
                throw new RuntimeException("Enrollement not found for id :: " + id);


        }

    //Manager
    public EventEnrollementDto createNewEnrollement(EventEnrollementDto enrollementDto ) {

        eventEnrollementValidator.validate(enrollementDto);
        Event event = eventDAO.getEventByEventName(enrollementDto.getEventName() ).orElseThrow(()->
                new EntityNotFoundException("Event name not Found "));

        Student student=studentDAO.findById(enrollementDto.getStudentID())
                .orElseThrow(()->new EntityNotFoundException("student not found "));

        EventEnrollement eventEnrollemet = eventEnrollementMapper.returnEventEnrollement(enrollementDto,event,student) ;


        enrollementDAO.save(eventEnrollemet);

        return eventEnrollementMapper.returnDto(eventEnrollemet);



    }
    //Student
    public EventEnrollementDto EnrollInEvent(Principal connectedUser, String eventName ) {

        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();


        if( ! user.getRole().equals(Role.STUDENT)) throw new IllegalArgumentException("this is only accessible by Students !!") ;


        Event event = eventDAO.getEventByEventName(eventName ).orElseThrow(()->
                new EntityNotFoundException("Event name not Found "));

        Student student=studentDAO.findById(user.getId())
                .orElseThrow(()->new EntityNotFoundException("student not found "));

EventEnrollement eventEnrollemet=EventEnrollement.builder()
        .enrollementDate(LocalDate.now())
        .event(event)
        .student(student)
        .build();

        enrollementDAO.save(eventEnrollemet);

        return eventEnrollementMapper.returnDto(eventEnrollemet);



    }

        public String removeEnrollement(Long id) {
            enrollementDAO.findById(id)
                    .orElseThrow(() -> new RuntimeException("Enrollement not found with id: " + id));

            enrollementDAO.deleteById(id);
            return "Enrollement removed successfully";

        }

    //manager
    public EventEnrollementDto updateEnrollement(EventEnrollementDto enrollementDto) {


        eventEnrollementValidator.validate(enrollementDto);

        EventEnrollement  enrollement =enrollementDAO.findById(enrollementDto.getId())
                .orElseThrow(()->new EntityNotFoundException("enrollement not found for update ")) ;

        Student student = studentDAO.findById(enrollementDto.getStudentID()).orElseThrow(()->new EntityNotFoundException("student not found"));
        Event event=eventDAO.getEventByEventName(enrollementDto.getEventName()).orElseThrow(()->new EntityNotFoundException("event not found "));

        enrollement.setEnrollementDate(enrollementDto.getEnrollementDate());
        enrollement.setRating(enrollementDto.getRating());
        enrollement.setStudent(student);
        enrollement.setEvent(event);
        enrollementDAO.save(enrollement);

        return eventEnrollementMapper.returnDto(enrollement);


    }

    public String Rate(Principal connectedUser ,Long formationId,Integer rating){
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();


        if( ! user.getRole().equals(Role.STUDENT)) throw new IllegalArgumentException("this is only accessible by Students !!") ;

        boolean isEnrolled = enrollementDAO.isStudentEnrolled(user.getId(),formationId);

        if(!isEnrolled) throw  new IllegalArgumentException("Student must be enrolled ! ") ;


        if(! (rating>0 && rating<=10) ) throw new IllegalArgumentException("rating must be between 0 and 10");


        Enrollement enrollement = enrollementDAO.findEnrollementByStudentIDAndFormationId(user.getId(),formationId);

        enrollement.setRating(rating);

        return "Enrollement Rated successfully :)" ;



    }


}

