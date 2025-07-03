package com.example.demo.controller;

import com.example.demo.Dtos.enrollementDto.EnrollementDto;
import com.example.demo.service.EnrollementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController

public class EnrollementController {

    @Autowired
    private EnrollementService enrollementService;

    @GetMapping("/getAllEnrollements")
    public ResponseEntity<List<EnrollementDto>> getAllEnrollements() {

        return new ResponseEntity<>(enrollementService.getAllEnrollements(), HttpStatus.OK);

    }
    @GetMapping("/getEnrollementsForStudent/{studentId}")
    public ResponseEntity <List<EnrollementDto>> getEnrollementsForStudent(@PathVariable Long studentId) {


        return new ResponseEntity<>(enrollementService.getEnrollementForStudent(studentId),HttpStatus.OK);


    }
    @GetMapping("/getEnrollementsForStudent")
    public ResponseEntity <List<EnrollementDto>> getEnrollementsForStudent(Principal connectedUser) {


        return new ResponseEntity<>(enrollementService.getEnrollementForStudent(connectedUser),HttpStatus.OK);


    }

    @GetMapping("/getFormationEnrollement/{formation_id}")

    public  ResponseEntity <List<EnrollementDto>> getFormationEnrollement(@PathVariable Long formation_id){


        return new  ResponseEntity<>(enrollementService.getFormationEnrollement(formation_id),HttpStatus.OK) ;

    }

    @GetMapping("/getEnrollementById/{id}")
    public ResponseEntity<EnrollementDto> getEnrollementById(@PathVariable Long id) {

        return new ResponseEntity<>(enrollementService.getEnrollementById(id),HttpStatus.OK);
    }

    @PostMapping("/createNewEnrollement")
    public ResponseEntity <EnrollementDto> createNewEnrollement(@RequestBody EnrollementDto enrollementDto) {

        return new ResponseEntity<>(enrollementService.createNewEnrollement(enrollementDto),HttpStatus.CREATED) ;

    }
    @PostMapping("/EnrollInFormation/{formation_name}")
    public ResponseEntity <EnrollementDto> createNewEnrollement(Principal connectedUser,@PathVariable String formation_name) {

        return new ResponseEntity<>(enrollementService.EnrollInFormation(connectedUser,formation_name),HttpStatus.CREATED) ;

    }


    @DeleteMapping("/removeEnrollement/{id}")
    public String removeEnrollement(@PathVariable Long id) {
      return   enrollementService.removeEnrollement(id);

    }


    @PutMapping("/updateEnrollement")
    public ResponseEntity<EnrollementDto> updateEnrollement(@RequestBody EnrollementDto enrollementDto) {

        return new ResponseEntity<>(enrollementService.updateEnrollement(enrollementDto),HttpStatus.OK) ;

    }

    @PostMapping("/rateFormation")
    public ResponseEntity<String> rateFormation(
            Principal connectedUser,
            @RequestParam Long formationId,
            @RequestParam Integer rating
    ) {

            String response = enrollementService.Rate(connectedUser, formationId, rating);
            return ResponseEntity.ok(response);

    }


}