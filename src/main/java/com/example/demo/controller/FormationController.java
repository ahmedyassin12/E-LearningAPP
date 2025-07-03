package com.example.demo.controller;

import com.example.demo.Dtos.formationDto.CreateFormationDto;
import com.example.demo.Dtos.formationDto.FormationDto;
import com.example.demo.Dtos.formationDto.FormationManagerDto;
import com.example.demo.Dtos.formationDto.FormationStudentDto;
import com.example.demo.entity.Formation;
import com.example.demo.service.FormationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class FormationController {

    @Autowired
    FormationService formationService ;

    @GetMapping("/getAllFormation")
    public ResponseEntity<Iterable<?>> getAllFormation(Principal connectedUser) {

        Iterable<?> formations = formationService.getAllFormation( connectedUser);

        return new ResponseEntity<>(formations, HttpStatus.OK);



    }

    @GetMapping("/getFormationsforEnrolledStudent") // Add path variable here
    public ResponseEntity<List<FormationStudentDto>> getFormationsforEnrolledStudent(Principal connectedUser) {
        List <FormationStudentDto> formationDtos = formationService.getFormationsforEnrolledStudent(connectedUser);
        return new ResponseEntity<>(formationDtos, HttpStatus.OK);
    }

    @GetMapping("/getFormationsForFormateur")
    public ResponseEntity<Iterable<?> >getFormationsForFormateur( Principal connectedUser) {

        Iterable<?>formations=formationService.getFormationsForFormateur(connectedUser);
        return new ResponseEntity<>(formations,HttpStatus.OK) ;

    }



    @GetMapping("/getFormationById/{id}")
    public ResponseEntity<FormationManagerDto> getFormationById(@PathVariable("id") Long id) {

        FormationManagerDto formationManagerDto = formationService.getFormationById(id);
        return new ResponseEntity<>(formationManagerDto, HttpStatus.OK);

    }

    @PostMapping({"/createNewFormation"})
    public ResponseEntity<FormationManagerDto> createNewFormation(@RequestBody CreateFormationDto createFormationDto) {


        return new ResponseEntity<>(formationService.createNewFormation(createFormationDto), HttpStatus.OK);



    }








    @GetMapping("/getFormationByName/{name}")
    public ResponseEntity<?> getFormationByNom(@PathVariable("name") String name, Principal connectedUser) {


        Object formation = formationService.getFormationByName(name,connectedUser);

        return new ResponseEntity<>(formation, HttpStatus.OK);


    }


    @DeleteMapping("/rem_formation/{id}")

    public ResponseEntity<?> rem_formation(@PathVariable("id")  Long id ){

        formationService.rem_Formation(id);
        return new ResponseEntity<>(HttpStatus.OK) ;


    }

    @PutMapping("/update_formation")
    public  ResponseEntity<FormationDto> update_formation(@RequestBody CreateFormationDto createFormationDto){


        return new ResponseEntity<>(formationService.update_formation(createFormationDto),HttpStatus.OK);

    }




}
