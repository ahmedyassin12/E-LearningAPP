package com.example.demo.controller;
import com.example.demo.Dtos.courseDto.CourseDto;
import com.example.demo.Dtos.courseDto.ManagerCourseDto;
import com.example.demo.Dtos.courseDto.CreateCourseDto;
import com.example.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/api/v3/course")

@RestController
public class CourseController {
    @Autowired
    CourseService courseService;


    @GetMapping("/getAllCourses")
    public ResponseEntity<List<ManagerCourseDto>> getAllCourses() {

        List<ManagerCourseDto> managerCourseDtos = courseService.getAllCourses();

        return new ResponseEntity<>(managerCourseDtos, HttpStatus.OK);

    }

    /*@GetMapping("/getStudentCourses/{student_id}/{formation_id}")
    public ResponseEntity<Iterable<Course>> getStudentCourses(@PathVariable Long student_id,@PathVariable Long formation_id){

        Iterable<Course>courses=courseService.findCoursesByStudentAndFormation(student_id,formation_id) ;

        if (courses==null ) System.out.println("u need  to pay for formation");

        return  new ResponseEntity<>(courses,HttpStatus.OK) ;

    }

    @GetMapping("/getFormateurCourses/{formateur_id}/{formation_id}")
    public ResponseEntity<List<CourseDto>>getFormateurCourses (@PathVariable Long formateur_id,@PathVariable Long formation_id){

        List<CourseDto>courseDtos=courseService.getFormateurCourses(formateur_id,formation_id) ;
        return  new ResponseEntity<>(courseDtos,HttpStatus.OK) ;



    }*/

    @GetMapping("/getFormationCourses/{formation_id}")
    public Iterable<?> getFormationCourses(@PathVariable Long formation_id,Principal connectedUser){

        Iterable<?> courses=courseService.getFormationCourses(formation_id,connectedUser);

        return courses;


    }


    @GetMapping("/getCourseById/{id}")
    public ResponseEntity<ManagerCourseDto> getCourseById(@PathVariable("id") Long id) {

        ManagerCourseDto courseDto = courseService.getCourseById(id);
        System.out.println("course  : " + courseDto);
        return new ResponseEntity<>(courseDto, HttpStatus.OK);

    }

    @PostMapping({"/createNewCourse"})
    public ResponseEntity<CourseDto> createNewCourse(@RequestBody CreateCourseDto createCourseDto) {


        return new ResponseEntity<>(courseService.createNewCourse(createCourseDto), HttpStatus.OK);



    }

    @GetMapping("/getCourseByNom/{nom}")
    public ResponseEntity<?> getCourseByNom(@PathVariable("nom") String courseName, Principal connectedUser) {

        Object result = courseService.getCourseByName(courseName, connectedUser);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    @DeleteMapping("/rem_course/{id}")

    public ResponseEntity<String> rem_lecture(@PathVariable("id")  Long id ){

        return new ResponseEntity<>(courseService.rem_Course(id),HttpStatus.OK) ;


    }

    @PutMapping("/update_Course")
    public  ResponseEntity<CourseDto> update_Course(@RequestBody CreateCourseDto updatedCourseDto){


        return new ResponseEntity<>(courseService.update_Course(updatedCourseDto),HttpStatus.OK);

    }


}
