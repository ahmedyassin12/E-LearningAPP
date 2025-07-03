package com.example.demo.mapper;

import com.example.demo.Dtos.EventEnrollementDto.EventEnrollementDto;
import com.example.demo.entity.Event;
import com.example.demo.entity.EventEnrollement;
import com.example.demo.entity.Student;

public class EventEnrollementMapper {


    public EventEnrollementDto returnDto(EventEnrollement e) {


        return EventEnrollementDto.builder()
                .enrollementDate(e.getEnrollementDate())
                .eventName(e.getEvent().getEventName())
                .studentID(e.getStudent().getId())
                .rating(e.getRating())
                .id(e.getId())
                .build();

    }

    public EventEnrollement returnEventEnrollement(EventEnrollementDto enrollementDto, Event event, Student student) {

        return EventEnrollement.builder()
                .event(event)
                .student(student)
                .Rating(enrollementDto.getRating())
                .id(enrollementDto.getId())
                .enrollementDate(enrollementDto.getEnrollementDate())
                .build();



    }
}
