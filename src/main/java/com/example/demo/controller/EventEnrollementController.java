package com.example.demo.controller;

import com.example.demo.Dtos.EventEnrollementDto.EventEnrollementDto;
import com.example.demo.entity.EventEnrollement;
import com.example.demo.service.EventEnrollementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class EventEnrollementController {

    @Autowired
    private EventEnrollementService enrollementService;

    @GetMapping("/getAllEventEnrollements")
    public ResponseEntity<List<EventEnrollementDto> > getAllEventEnrollements() {
        return new ResponseEntity<>(enrollementService.getAllEnrollements(), HttpStatus.OK) ;
    }

    //manager
    @GetMapping("/getEventEnrollementForStudent/{studentId}")
    public ResponseEntity <Iterable<EventEnrollementDto>> getEventEnrollementForStudent(@PathVariable("studentId") Long studentId) {
        return new ResponseEntity<>(enrollementService.getEventEnrollementForStudent(studentId),HttpStatus.OK );


    }

    //student
    @GetMapping("/getEventEnrollementForStudent")
    public ResponseEntity <Iterable<EventEnrollementDto>> getEventEnrollementForStudent(Principal connectedUser) {

        return new ResponseEntity<>(enrollementService.getEventEnrollementForStudent(connectedUser),HttpStatus.OK );


    }


    //get Enrollement of that specific event 
    @GetMapping("/getEventEnrollementForEvent/{event_id}")
    public ResponseEntity<List<EventEnrollementDto>> getEventEnrollementForEvent(@PathVariable("event_id")Long event_id){

        return new ResponseEntity<>(enrollementService.getEventEnrollement(event_id),HttpStatus.OK) ;

    }


    @GetMapping("/getEventEnrollementById/{id}")
    public ResponseEntity <EventEnrollementDto> getEventEnrollementById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(enrollementService.getEnrollementById(id),HttpStatus.OK);
    }

    @PostMapping("/createNewEventEnrollement")
    public ResponseEntity<EventEnrollementDto> createNewEventEnrollement(@RequestBody EventEnrollementDto enrollement) {
        return new ResponseEntity<>(enrollementService.createNewEnrollement(enrollement),HttpStatus.OK);
    }

    @PostMapping("/EnrollInEvent/{event_name}")
    public ResponseEntity<EventEnrollementDto> createNewEventEnrollement(Principal connectedUser,@PathVariable String event_name) {
        return new ResponseEntity<>(enrollementService.EnrollInEvent(connectedUser,event_name),HttpStatus.OK);
    }


    @DeleteMapping("/removeEventEnrollement/{id}")
    public ResponseEntity<String> removeEventEnrollement(@PathVariable("id") Long id) {
       return new ResponseEntity<>(enrollementService.removeEnrollement(id),HttpStatus.OK);
    }

    @PutMapping("/updateEventEnrollement")
    public ResponseEntity <EventEnrollementDto> updateEventEnrollement(@RequestBody EventEnrollementDto enrollementDto) {



        return new ResponseEntity<>(enrollementService.updateEnrollement(enrollementDto),HttpStatus.OK);



    }

    @PostMapping("/rateEvent")
    public ResponseEntity<String> rateEvent(
            Principal connectedUser,
            @RequestParam Long EventId,
            @RequestParam Integer rating
    ) {

        String response = enrollementService.Rate(connectedUser, EventId, rating);
        return ResponseEntity.ok(response);

    }
}
