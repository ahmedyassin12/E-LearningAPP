package com.example.demo.controller;

import com.example.demo.Dtos.userDto.CreateFormateurDto;
import com.example.demo.Dtos.userDto.FormateurDto;
import com.example.demo.entity.Enums.Availability;
import com.example.demo.service.FormateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class FormateurController {



        @Autowired
        FormateurService formateurService ;


        @RequestMapping("/testFormateur")
        public String test() {
            return ("Je fonctionne correctement");
        }


/*
        @PostConstruct
        public void initFormateur () throws ParseException {



            formateurService.initFormateur();



        }
*/


        @GetMapping("/getAllFormateurs")
        public ResponseEntity<List<FormateurDto>> getAllFormateurs() {


            return new ResponseEntity<>(formateurService.getallFormateur(), HttpStatus.OK);



        }
        @GetMapping("/getFormateurByid/{formateurId}")
        public ResponseEntity<FormateurDto> getFormateurByid(@PathVariable("formateurId") int formateurId) {

            return new ResponseEntity<>(formateurService.getFormateurById(formateurId), HttpStatus.OK);

        }
    @GetMapping("/getFormateurByUsername/{username}")
    public ResponseEntity<FormateurDto> getFormateurByUsername(@PathVariable("username") String username) {

        return new ResponseEntity<>(formateurService.getFormateurByUsername(username), HttpStatus.OK);

    }


        @PostMapping({"/createNewFormateur"})
        public ResponseEntity<FormateurDto> createNewFormateur(@RequestBody CreateFormateurDto createFormateurDto) {


            return new ResponseEntity<>(formateurService.createNewFormateur(createFormateurDto), HttpStatus.OK);



        }

        @GetMapping("/getFormateurByFirstName/{firstName}")
        public ResponseEntity<FormateurDto> getFormateurByNom(@PathVariable("firstName") String firstName) {

            return new ResponseEntity<>(formateurService.getFormateurByFirstName(firstName), HttpStatus.OK);

        }
    @GetMapping("/getFormateurByEmail/{Email}")
    public ResponseEntity<FormateurDto> getFormateurByEmail(@PathVariable("Email") String Email) {

        return new ResponseEntity<>(formateurService.getFormateurByEmail(Email), HttpStatus.OK);

    }

        @DeleteMapping("/rem_formateur/{id}")

        public ResponseEntity<?> rem_formateur(@PathVariable("id")  int id ){

            formateurService.rem_formateur(id);
            return new ResponseEntity<>(HttpStatus.OK) ;


        }

        @PutMapping("/update_formateur")
        public  ResponseEntity<FormateurDto> update_formateur(@RequestBody FormateurDto formateurDto){


            return new ResponseEntity<>(formateurService.update_formateur(formateurDto),HttpStatus.OK);

        }



        @GetMapping("/formateurGetMyProfile")
    public ResponseEntity<FormateurDto> getMyProfile(Principal connectedUser)
        {


            return new ResponseEntity<>(formateurService.getMyProfile(connectedUser),HttpStatus.OK);

        }

        @PatchMapping("/UpdateAvailability")
    public ResponseEntity<String> UpdateAvailability(@RequestBody Availability availability,Principal connectedUser){

            return new ResponseEntity<>(formateurService.UpdateAvailability(availability,connectedUser),HttpStatus.OK);

        }




}
