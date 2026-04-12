package com.example.demo.service;
import com.example.demo.Dtos.formationDto.FormationManagerDto;
import com.example.demo.Dtos.formationDto.FormationStudentDto;
import com.example.demo.dao.EnrollementDAO;
import com.example.demo.dao.FormationDAO;
import com.example.demo.entity.Enums.Role;
import com.example.demo.entity.Formation;
import com.example.demo.entity.User;
import com.example.demo.mapper.FormationMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FormationCacheService {


    @Autowired
    private FormationDAO formationDAO ;
    @Autowired
    private EnrollementDAO enrollementDAO;


    private FormationMapper formationMapper =new FormationMapper() ;




    @Cacheable(value = "studentFormation",key = "#user.id + '_' + #formationName", unless = "#result == null || " +
            "!(#result instanceof T(com.example.demo.Dtos.formationDto.FormationStudentDto))")
    public Object GetchachedFormationByName(User user, String formationName){


        if (user.getRole() == Role.FORMATEUR) {
            Formation formation = formationDAO.findFormationByNameForFormateur(formationName, user.getId())
                    .orElseThrow(() -> new AccessDeniedException(
                            "Formateur not associated with formation: " + formationName
                    ));
            return formationMapper.returnformationDto(formation) ;


        }

        // Handle other roles
        Formation formation = formationDAO.findFormationByName(formationName)
                .orElseThrow(() -> new EntityNotFoundException(formationName));

        if (user.getRole() == Role.STUDENT) {
            System.out.println("Get Formation From DB");
            boolean isPaid= enrollementDAO.isEnrollmentPaid(user.getId(), formation.getId());
            boolean isEnrolled= enrollementDAO.isStudentEnrolled(user.getId(), formation.getId());

            return formationMapper.returnformationStudentDto(formation,isPaid,isEnrolled);

        }

        if (user.getRole() == Role.MANAGER) {
            return  formationMapper.returnformationManagerDto(formation);
        }

        throw new AccessDeniedException("Unauthorized role: " + user.getRole());

    }


    @Cacheable(value = "AllStudentFormations",key = "#user.id", unless = "#result == null || " +
            "!(#result instanceof T(com.example.demo.Dtos.formationDto.FormationStudentDto))")
    public Iterable<?> CachedGetAllFormations(User user) {

        Iterable<Formation> formations = formationDAO.findAll();

        if (formations.iterator().hasNext()) {

            if (user.getRole().equals(Role.MANAGER)) {

                List<FormationManagerDto> formationManagerDtos = new ArrayList<>();

                formations.forEach(formation ->
                        formationManagerDtos.add(
                                formationMapper.returnformationManagerDto(formation)
                        ));


                return formationManagerDtos;

            } else if (user.getRole() == Role.STUDENT) {

                boolean isPaid;
                boolean isEnrolled;

                List<FormationStudentDto> formationStudentDtos = new ArrayList<>();

                for (Formation formation : formations) {
                    isEnrolled = enrollementDAO.isStudentEnrolled(user.getId(), formation.getId());
                    isPaid = enrollementDAO.isEnrollmentPaid(user.getId(), formation.getId());

                    formationStudentDtos.add(formationMapper.returnformationStudentDto(formation, isPaid, isEnrolled));


                }

                return formationStudentDtos;

            }
        }

        throw new EntityNotFoundException("Formations are empty");

    }

}
