package com.example.demo.controller;
import com.example.demo.Dtos.userDto.CreateStudentDto;
import com.example.demo.Dtos.userDto.FormateurDto;
import com.example.demo.Dtos.userDto.StudentDto;
import com.example.demo.entity.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
public class StudentController {


@Autowired
    StudentService studentService ;




  /*  @PostConstruct
    public void initStudents () throws ParseException {


        studentService.initStudent();



    }*/
  @GetMapping("/getStudentOfFormation/{formation_id}")
  public ResponseEntity<List<StudentDto>>getStudentOfFormation ( @PathVariable Long formation_id){

      return  new ResponseEntity<>(studentService.getStudentOfFormation(formation_id) ,HttpStatus.OK) ;



  }


    @GetMapping("/getAllStudents")
    public ResponseEntity<List<StudentDto>> getAllStudents() {


        return new ResponseEntity<>(studentService.getAllStudents(), HttpStatus.OK);

    }
    @GetMapping("/getStudentByUsername/{username}")
    public ResponseEntity<StudentDto> getStudentByUsername(@PathVariable("username") String username ) {


        return new ResponseEntity<>(studentService.getStudentByUsername(username), HttpStatus.OK);

    }

    @GetMapping("/getStudentById/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable("id") Long studentID) {


        return new ResponseEntity<>(studentService.getstudentbyId(studentID), HttpStatus.OK);

    }

    @PostMapping({"/createNewStudent"})
    public ResponseEntity<StudentDto> createNewStudent(@RequestBody CreateStudentDto createStudentDto) {


        return new ResponseEntity<>(studentService.createNewStudent(createStudentDto), HttpStatus.OK);



    }

    @GetMapping("/getStudentByFirstName/{firstName}")
    public ResponseEntity<StudentDto> getStudentbyFirstName(@PathVariable("firstName") String firstName) {

        return new ResponseEntity<>(studentService.getStudentbyFirstName(firstName), HttpStatus.OK);

    }

    @GetMapping("/getStudentByEmail/{Email}")
    public ResponseEntity<StudentDto> getStudentByEmail(@PathVariable("Email") String Email) {

        return new ResponseEntity<>(studentService.getStudentByEmail(Email), HttpStatus.OK);

    }


    @DeleteMapping("/rem_student/{id}")

    public ResponseEntity<?> rem_student(@PathVariable("id")  Long id ){

        studentService.rem_student(id);
return new ResponseEntity<>(HttpStatus.OK) ;


    }

    @PutMapping("/update_student")
    public ResponseEntity<StudentDto> updateStudent(@RequestBody StudentDto studentDto) {
        return ResponseEntity.ok(studentService.update_student(studentDto));
    }



    @GetMapping("/StudentGetMyProfile")
    public ResponseEntity<StudentDto> getMyProfile(Principal connectedUser)
    {


        return new ResponseEntity<>(studentService.getMyProfile(connectedUser),HttpStatus.OK);

    }

}
