package com.example.demo.dao;

import com.example.demo.entity.Formateur;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface FormateurDAO extends CrudRepository<Formateur,Long> {



    Optional<Formateur> findFormateurByEmail(String Email);


    Optional<Formateur> findFormateurByFirstName(String firstName);


    Optional<Formateur> findFormateurByUsername(String username);

}
