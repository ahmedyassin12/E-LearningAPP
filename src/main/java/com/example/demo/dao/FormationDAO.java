package com.example.demo.dao;

import com.example.demo.entity.Formation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FormationDAO extends CrudRepository<Formation,Long> {


    @Query("SELECT f FROM Formation f  WHERE f.formationName =:name")
    Optional<Formation> findFormationByName(String name);

    @Query("""
    SELECT f FROM Formation f
    JOIN f.formateurs fo
    WHERE f.formationName = :name
    AND fo.id = :formateurId
""")
    Optional<Formation> findFormationByNameForFormateur(@Param("name") String name, @Param("formateurId") Long formateurId);


    @Query("SELECT f FROM Formation f JOIN f.formateurs fm WHERE fm.id = :formateur_id")
    public Iterable<Formation> getFormationsForFormateur(Long formateur_id );


    @Query("SELECT f FROM Formation f JOIN Enrollement e ON f.id = e.formation.id WHERE e.student.id = :student_id")
    public Iterable<Formation> getFormationsforEnrolledStudent(Long student_id );




}
