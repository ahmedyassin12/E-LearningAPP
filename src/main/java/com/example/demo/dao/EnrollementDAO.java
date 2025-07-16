package com.example.demo.dao;

import com.example.demo.entity.Enrollement;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollementDAO extends CrudRepository<Enrollement,Long> {



    @Query("SELECT e FROM Enrollement e WHERE e.student.id = :studentId")
    Iterable<Enrollement> getEnrollementForStudent(Long studentId);

    @Query("""
    SELECT COUNT(e) > 0 FROM Enrollement e 
    WHERE e.student.id = :studentId 
      AND e.formation.id = :formationId 
      AND e.payment_Status = 'Paid'
""")
    boolean isEnrollmentPaid(@Param("studentId") Long studentId, @Param("formationId") Long formationId);

    @Query("""
    SELECT COUNT(e) > 0 FROM Enrollement e 
    WHERE e.student.id = :studentId 
      AND e.formation.id = :formationId """)
    boolean isStudentEnrolled(@Param("studentId")Long studentId,@Param("formationId")Long formationId);

    @Query("SELECT e FROM Enrollement e " +
            "JOIN e.formation f " +
            "WHERE f.id = :formation_id")
    Iterable<Enrollement> getFormationEnrollement(@Param("formation_id") Long formation_id);


    @Query("SELECT e FROM Enrollement e WHERE e.student.id = :studentId AND e.formation.id = :formationId")

    Enrollement findEnrollementByStudentIDAndFormationId(@Param("studentId") Long studentId,@Param("formationId") Long formationId);



}
