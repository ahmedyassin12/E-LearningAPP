package com.example.demo.controller;

import com.example.demo.Dtos.eventDto.CreateEventDto;
import com.example.demo.Dtos.eventDto.EventDto;
import com.example.demo.Dtos.eventDto.EventManagerDto;
import com.example.demo.entity.Event;
import com.example.demo.service.EventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/v5/event")

public class EventController {

    @Autowired
    private EventService eventService;


    @GetMapping("/getAllEvents")
    public ResponseEntity<Iterable<?>> getAllEvents(Principal connectedUser) {
        return new ResponseEntity<>(eventService.getAllEvents(connectedUser),HttpStatus.OK);

    }




    @GetMapping("/getEventById/{id}")
    public ResponseEntity<EventManagerDto> getEventById(@PathVariable Long id) {
        return  new ResponseEntity<>(eventService.getEventById(id),HttpStatus.OK);
    }

    @GetMapping("/getEventByName/{name}")
    public ResponseEntity<Object> getEventByName(@PathVariable String name,Principal connectedUser) {
        return new ResponseEntity<>(eventService.getEventByName(name,connectedUser),HttpStatus.OK);
    }
    @GetMapping("/getEventByStreetLocation/{streetLocation}")
    public ResponseEntity<Object> getEventByStreetLocation(@PathVariable String streetLocation,Principal connectedUser) {
        return new ResponseEntity<>(eventService.getEventByStreetLocation(streetLocation,connectedUser),HttpStatus.OK);
    }

    @GetMapping("/getEventForFormateur")
    public ResponseEntity<List<EventDto>> getEventsForFormateur(Principal connectedUser  ) {


        return new ResponseEntity<>(eventService.getEventsForFormateur(connectedUser),HttpStatus.OK) ;


    }

    @PostMapping("/createNewEvent")
    public ResponseEntity<EventManagerDto>  createNewEvent(@RequestBody CreateEventDto createEventDto) {





        return new ResponseEntity<>(eventService.createNewEvent(createEventDto),HttpStatus.OK);

    }

    @DeleteMapping("/rem_event/{id}")
    public ResponseEntity<String>  rem_event(@PathVariable Long id) {

       return new ResponseEntity<>(eventService.rem_event(id),HttpStatus.OK);

    }

    @PutMapping("/update_event")
    public ResponseEntity<EventDto> update_event(@RequestBody CreateEventDto createEventDto ) {

        return new ResponseEntity<>(eventService.update_event(createEventDto),HttpStatus.OK);

    }

}