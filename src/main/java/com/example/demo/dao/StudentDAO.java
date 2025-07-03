package com.example.demo.dao;

import com.example.demo.entity.Enrollement;
import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository

public interface StudentDAO extends CrudRepository<Student,Long>   {


Optional<Student> findStudentByEmail(String Email);


Optional<Student> findStudentByFirstName(String firstName);


    Optional<Student> findStudentByUsername(String username);

    @Query("SELECT s FROM Enrollement e JOIN e.student s WHERE e.formation.id = :formationId")
    Iterable<Student> getStudentsByFormation(@Param("formationId") Long formationId);


    @Query("SELECT s.publicId  FROM Student s WHERE s.id = :studentId")
    String getPublicIdByStudent(@Param("studentId") Long studentId);




}
