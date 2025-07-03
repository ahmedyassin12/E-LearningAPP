package com.example.demo.dao;

import com.example.demo.entity.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventDAO extends CrudRepository<Event,Long> {

    Optional<Event> getEventByEventName(String eventName);

    Optional<Event> getEventByStreetLocation(String streetLocation);



    @Query("SELECT e FROM Event e JOIN e.enrollements ee WHERE ee.student.id = :studentId")
    Iterable<Event> getEventForEnrolledStudent(@Param("studentId") Long studentId);



    @Query("""
    SELECT e FROM Event e
    JOIN e.formateurs fo
    WHERE e.eventName = :eventName
    AND fo.id = :formateurId
""")
    Optional<Event> findEventByNameForFormateur(String eventName, Long formateurId);

    @Query("SELECT e FROM Event e JOIN e.formateurs fm WHERE fm.id = :formateurId")

    Iterable<Event> getEventsForFormateur(Long formateurId);
}
