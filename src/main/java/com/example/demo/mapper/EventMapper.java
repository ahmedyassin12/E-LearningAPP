package com.example.demo.mapper;

import com.example.demo.Dtos.eventDto.CreateEventDto;
import com.example.demo.Dtos.eventDto.EventDto;
import com.example.demo.Dtos.eventDto.EventManagerDto;
import com.example.demo.Dtos.eventDto.EventStudentDto;
import com.example.demo.entity.Event;
import com.example.demo.entity.Formateur;

import java.util.Set;

public class EventMapper {
    public EventManagerDto returnEventManagerDto(Event event) {
        return EventManagerDto.builder()
                .eventId(event.getEvent_id())
                .date(event.getDate())
                .EventName(event.getEventName())
                .description(event.getDescription())
                .cityLocation(event.getCityLocation())
                .imageUrl(event.getImageUrl())
                .publicId(event.getPublicId())
                .streetLocation(event.getStreetLocation())
                .build();
    }

    public EventStudentDto returnEventStudentDto(Event event, boolean isEnrolled) {

        return EventStudentDto.builder()
                .eventId(event.getEvent_id())
                .date(event.getDate())
                .EventName(event.getEventName())
                .description(event.getDescription())
                .cityLocation(event.getCityLocation())
                .imageUrl(event.getImageUrl())
                .streetLocation(event.getStreetLocation())
                .isStudentEnrolled(isEnrolled)
                .build();



    }

    public EventDto returnEventDto(Event event) {

        return EventDto.builder()
                .eventId(event.getEvent_id())
                .date(event.getDate())
                .EventName(event.getEventName())
                .description(event.getDescription())
                .cityLocation(event.getCityLocation())
                .imageUrl(event.getImageUrl())
                .streetLocation(event.getStreetLocation())
                .build();
    }

    public Event ToEventForCreation(CreateEventDto createEventDto, Set<Formateur> formateurs) {


        return Event.builder()
                .event_id(createEventDto.getEventDto().getEventId())
                .date(createEventDto.getEventDto().getDate())
                .eventName(createEventDto.getEventDto().getEventName())
                .description(createEventDto.getEventDto().getDescription())
                .cityLocation(createEventDto.getEventDto().getCityLocation())
                .imageUrl(createEventDto.getEventDto().getImageUrl())
                .streetLocation(createEventDto.getEventDto().getStreetLocation())
                .formateurs(formateurs)

                .build();

    }

    public Event returnEvent(CreateEventDto createEventDto, Event event) {

        event.setEventName(createEventDto.getEventDto().getEventName());
        event.setDate(createEventDto.getEventDto().getDate());
        event.setCityLocation(createEventDto.getEventDto().getCityLocation());
        event.setStreetLocation(createEventDto.getEventDto().getStreetLocation());
        event.setDescription(createEventDto.getEventDto().getDescription());
        return  event ;
    }
}
