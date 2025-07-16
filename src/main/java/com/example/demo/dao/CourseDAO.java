package com.example.demo.dao;

import com.example.demo.entity.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseDAO extends CrudRepository<Course,Long> {


    Optional<Course> findCourseBycourseName(String courseName);


    @Query("SELECT c FROM Course c JOIN c.formation f " +
            "JOIN f.formateurs fm" +
            " WHERE fm.id = :formateur_id AND f.id = :formationId")
    public Iterable<Course> getFormateurCourses(@Param("formateur_id") Long formateur_id, @Param("formationId") Long formationId);


    @Query("SELECT c FROM Course c " +
            "JOIN c.formation f " +
            "JOIN Enrollement e ON e.formation.id = f.id " +
            "WHERE e.student.id = :studentId " +
            "AND e.formation.id = :formationId " +
            "AND e.payment_Status = 'Paid'")
    public Iterable<Course> findCoursesByStudentAndFormation(@Param("studentId") Long studentId, @Param("formationId") Long formationId);



    @Query("SELECT c FROM Course c " +
            "JOIN c.formation f " +
            "JOIN Enrollement e ON e.formation.id = f.id " +
            "WHERE e.formation.id = :formation_id")
    public Iterable<Course> getFormationCourses(@Param("formation_id") Long formation_id);




}
