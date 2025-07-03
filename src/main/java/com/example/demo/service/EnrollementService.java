package com.example.demo.service;

import com.example.demo.Dtos.enrollementDto.EnrollementDto;
import com.example.demo.dao.EnrollementDAO;
import com.example.demo.dao.FormationDAO;
import com.example.demo.dao.StudentDAO;
import com.example.demo.entity.*;
import com.example.demo.entity.Enums.PaymentStatus;
import com.example.demo.entity.Enums.Role;
import com.example.demo.mapper.EnrollementMapper;
import com.example.demo.validator.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EnrollementService {

    @Autowired
    private EnrollementDAO enrollementDAO;

    private EnrollementMapper enrollementMapper=new EnrollementMapper() ;
    @Autowired
    private FormationDAO formationDAO;
    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private ObjectValidator<EnrollementDto>enrollementValidator ;


    //manager:
    public List<EnrollementDto> getAllEnrollements() {


        Iterable<Enrollement>enrollements= enrollementDAO.findAll() ;

        if(enrollements.iterator().hasNext()){
            List<EnrollementDto> enrollementDtos=new ArrayList<>();

            enrollements.forEach(enrollement ->
                    enrollementDtos.add(
                            enrollementMapper.returnEnrollementDto(enrollement)
                    ) );


            return enrollementDtos ;

        }


        throw new EntityNotFoundException("No Enrollements found") ;





    }

    //Manager
    public List<EnrollementDto> getEnrollementForStudent(Long student_id ) {


        Iterable<Enrollement> enrollements=enrollementDAO.getEnrollementForStudent(student_id) ;


        if(enrollements.iterator().hasNext()){
            List<EnrollementDto> enrollementDtos=new ArrayList<>();

            enrollements.forEach(enrollement ->
                    enrollementDtos.add(
                            enrollementMapper.returnEnrollementDto(enrollement)
                    ) );


            return enrollementDtos ;

        }


        throw new EntityNotFoundException("No Enrollements found") ;

    }

    //Student
    public List<EnrollementDto> getEnrollementForStudent(Principal connectedUser ) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal() ;

        Iterable<Enrollement> enrollements=enrollementDAO.getEnrollementForStudent(user.getId()) ;


        if(enrollements.iterator().hasNext()){
            List<EnrollementDto> enrollementDtos=new ArrayList<>();

            enrollements.forEach(enrollement ->
                    enrollementDtos.add(
                            enrollementMapper.returnEnrollementDto(enrollement)
                    ) );


            return enrollementDtos ;

        }


        throw new EntityNotFoundException("No Enrollements found") ;

    }


    //manager
    public   List<EnrollementDto> getFormationEnrollement(Long formation_id){


        Iterable<Enrollement> enrollements=enrollementDAO.getFormationEnrollement(formation_id);


        if(enrollements.iterator().hasNext()){
            List<EnrollementDto> enrollementDtos=new ArrayList<>();

            enrollements.forEach(enrollement ->
                    enrollementDtos.add(
                            enrollementMapper.returnEnrollementDto(enrollement)
                    ) );


            return enrollementDtos ;

        }


        throw new EntityNotFoundException("No Enrollements found") ;

  }



  //manager
    public EnrollementDto getEnrollementById(Long id) {
        Optional<Enrollement> optional = enrollementDAO.findById(id);
        if (optional.isPresent()) {

            return enrollementMapper.returnEnrollementDto(optional.get()) ;
        }


            throw new RuntimeException("Enrollement not found for id :: " + id);

    }

    //Manager
    public EnrollementDto createNewEnrollement(EnrollementDto enrollementDto ) {

        enrollementValidator.validate(enrollementDto);

        Formation formation = formationDAO.findFormationByName(enrollementDto.getFormationName()).orElseThrow(()->
                new EntityNotFoundException("Formation name not Found "));

        Student student=studentDAO.findById(enrollementDto.getStudentId())
                .orElseThrow(()->new EntityNotFoundException("student not found "));

        Enrollement enrollement = enrollementMapper.returnEnrollement(enrollementDto,formation,student) ;


        enrollementDAO.save(enrollement);
        log.info("Enrollement {} is saved", enrollement.getEnrollement_id());
        return enrollementMapper.returnEnrollementDto(enrollement);



    }

    //student
    public EnrollementDto EnrollInFormation(Principal connectedUser, String formationName ) {
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();


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
        log.info("Enrollement {} is saved", enrollement.getEnrollement_id());
        return enrollementMapper.returnEnrollementDto(enrollement);



    }

    //manager
    public String removeEnrollement(Long id) {
        enrollementDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollement not found with id: " + id));

        enrollementDAO.deleteById(id);
        return "Enrollement deleted successfully";

    }

    //manager
    public EnrollementDto updateEnrollement(EnrollementDto enrollementDto) {


        enrollementValidator.validate(enrollementDto);
        Enrollement enrollement =enrollementDAO.findById(enrollementDto.getEnrollementId())
                .orElseThrow(()->new EntityNotFoundException("enrollement not found for update ")) ;

        Formation formation=formationDAO.findFormationByName(enrollementDto.getFormationName()).orElseThrow(()->new EntityNotFoundException("formation name not Found"));

        Student student=studentDAO.findById(enrollementDto.getStudentId()).orElseThrow(()->new EntityNotFoundException("student not foudn"));

        enrollement.setPayment_Status(enrollementDto.getPaymentStatus());
        enrollement.setEnrollement_date(enrollementDto.getEnrollementDate());
        enrollement.setRating(enrollementDto.getRating());
        enrollement.setFormation(formation);
        enrollement.setStudent(student);
         enrollementDAO.save(enrollement);

         return enrollementMapper.returnEnrollementDto(enrollement);

    }


    public String Rate(Principal connectedUser ,Long formationId,Integer rating){
        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();


        if( ! user.getRole().equals(Role.STUDENT)) throw new IllegalArgumentException("this is only accessible by Students !!") ;

        boolean isEnrolled = enrollementDAO.isStudentEnrolled(user.getId(),formationId);

        if(!isEnrolled) throw  new IllegalArgumentException("Student must be enrolled ! ") ;


        if(! (rating>0 && rating<=10) ) throw new IllegalArgumentException("rating must be between 0 and 10");


        Enrollement enrollement = enrollementDAO.findEnrollementByStudentIDAndFormationId(user.getId(),formationId);



        enrollement.setRating(rating);

        return "Enrollement Rated successfully :)" ;



    }


}