package com.example.demo.controller;

import com.example.demo.Dtos.userDto.CreateUserDto;
import com.example.demo.Dtos.userDto.StudentDto;
import com.example.demo.Dtos.userDto.UserDto;
import com.example.demo.auth.Dto.ChangePasswordRequest;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.User;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UserController {


    @Autowired
    private UserService userService;


    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUsers() {


        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);


    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return new ResponseEntity<>(userService.getUserbyId(id), HttpStatus.OK);
    }

    @GetMapping("/findUserByEmail/{email}")
    public ResponseEntity<UserDto> findUserByEmail(@PathVariable String email) {

        return new ResponseEntity<>(userService.findUserByEmail(email), HttpStatus.OK);

    }
    @GetMapping("/getUserByUsername/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {

        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);


    }


    @PostMapping ("/createNewUser")
    public ResponseEntity<UserDto> createNewUser(@RequestBody CreateUserDto createUserDto) {


        return new ResponseEntity<>(userService.createNewUser(createUserDto), HttpStatus.CREATED);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto  userDto ) {


        return new ResponseEntity<>(userService.update_user(userDto), HttpStatus.OK);



    }



    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.rem_user(id);
        return ResponseEntity.ok("user removed");
    }


    @PatchMapping("/changePassword")
    public ResponseEntity<?>changePaassword(
            @RequestBody ChangePasswordRequest request
            ,
            Principal connectedUser
    )
    {


        userService.changePassword(request,connectedUser) ;

        return  ResponseEntity.ok().build() ;

    }

    @PatchMapping("/ManagerChangePassword")
    public ResponseEntity<?>ManagerChangePaassword(
            @RequestBody ChangePasswordRequest request
            ,
            Long userId
    )
    {


        userService.changePassword(request,userId) ;

        return  ResponseEntity.ok().build() ;

    }

    @GetMapping("/managerGetMyProfile")
    public ResponseEntity<UserDto> getMyProfile(Principal connectedUser)
    {


        return new ResponseEntity<>(userService.getMyProfile(connectedUser),HttpStatus.OK);

    }


}