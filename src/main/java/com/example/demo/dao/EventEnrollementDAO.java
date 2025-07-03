package com.example.demo.dao;

import com.example.demo.entity.Enrollement;
import com.example.demo.entity.EventEnrollement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventEnrollementDAO extends CrudRepository<EventEnrollement,Long> {



    @Query("SELECT e FROM EventEnrollement e WHERE e.student.id = :studentId")
    Iterable<EventEnrollement> getEventEnrollementForStudent(Long studentId);


    @Query("SELECT e FROM EventEnrollement e " +
            "JOIN e.event f " +
            "WHERE f.event_id = :event_id")
    Iterable<EventEnrollement> getEventEnrollement(@Param("event_id") Long event_id);


    @Query("""
    SELECT COUNT(e) > 0 FROM EventEnrollement  e 
    WHERE e.student.id = :studentId 
      AND e.event.event_id = :eventId """)
    boolean isStudentEnrolled(Long studentId, Long eventId);


    @Query("SELECT e FROM EventEnrollement e WHERE e.student.id = :studentId AND e.event.event_id = :eventId")
    Enrollement findEnrollementByStudentIDAndFormationId(@Param("studentId") Long studentId, @Param("eventId") Long eventId);




}

