package com.example.demo.auth.Controller;

import com.example.demo.auth.Dto.*;
import com.example.demo.auth.Service.AuthenticationService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
@Builder
public class AuthenticationController {

    private final AuthenticationService service;



    @PostMapping("/registerManager")
    public ResponseEntity<String> register (@RequestBody RegisterRequest request ){

       //

return ResponseEntity.ok(service.registerManager(request)) ;


    }
    @PostMapping("/registerStudent")
    public ResponseEntity<String> register (@RequestBody StudentRequest request ){

        //

        return ResponseEntity.ok(service.registerStudent(request)) ;


    }
    @PostMapping("/registerFormateur")
    public ResponseEntity<String> register (@RequestBody FormateurRequest request ){

        //

        return ResponseEntity.ok(service.registerFormateur(request)) ;


    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate
            ( @RequestBody AuthenticationRequest request ) throws BadRequestException {

        //
        return ResponseEntity.ok(service.authenticate(request)) ;


    }





}
