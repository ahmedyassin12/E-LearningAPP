package com.example.demo.service;

import com.example.demo.Dtos.enrollementDto.EnrollementDto;
import com.example.demo.dao.EnrollementDAO;
import com.example.demo.dao.FormationDAO;
import com.example.demo.dao.StudentDAO;
import com.example.demo.entity.Enrollement;
import com.example.demo.entity.Enums.PaymentStatus;
import com.example.demo.entity.Enums.Role;
import com.example.demo.entity.Formation;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.mapper.EnrollementMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EnrollementCacheService {

    @Autowired
    private FormationDAO formationDAO;
    @Autowired
    private StudentDAO studentDAO;
    @Autowired
    private EnrollementDAO enrollementDAO;

    private EnrollementMapper enrollementMapper=new EnrollementMapper() ;

    @Caching(evict = {
            @CacheEvict(cacheNames = "studentFormation",key = "#user.id+'_'+#formationName"),
            @CacheEvict(cacheNames = "AllStudentFormations", key = "#user.id")
    })    public EnrollementDto CachedEnrollInFormation(User user, String formationName) {

        if( ! user.getRole().equals(Role.STUDENT)) throw new IllegalArgumentException("this is only accessible by Students !!") ;


        Formation formation = formationDAO.findFormationByName(formationName).orElseThrow(()->
                new EntityNotFoundException("Formation name not Found "));

        Student student=studentDAO.findById(user.getId()).orElseThrow(()->new EntityNotFoundException("student not found "));

        Enrollement enrollement = Enrollement.builder()
                .payment_Status(PaymentStatus.UnPaid)
                .enrollement_date(LocalDate.now())
                .formation(formation)
                .student(student)
                .build();


        enrollementDAO.save(enrollement);
        return enrollementMapper.returnEnrollementDto(enrollement);




    }

}
