package com.example.demo.service;

import com.example.demo.Dtos.courseDto.CourseDto;
import com.example.demo.Dtos.courseDto.UnpaidCourseDto;
import com.example.demo.Dtos.courseDto.ManagerCourseDto;
import com.example.demo.Dtos.courseDto.CreateCourseDto;
import com.example.demo.dao.CourseDAO;
import com.example.demo.dao.EnrollementDAO;
import com.example.demo.dao.FormationDAO;
import com.example.demo.entity.*;
import com.example.demo.entity.Enums.Role;
import com.example.demo.mapper.CourseMapper;
import com.example.demo.validator.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CourseService {

    @Autowired
    private CourseDAO courseDAO;


    @Autowired
    private CourseCacheService courseCacheService;

    @Autowired
    private FormationDAO formationDAO ;

    @Autowired
    EnrollementDAO enrollementDAO ;

    @Autowired
private ObjectValidator<CreateCourseDto> courseValidator ;



    CourseMapper courseMapper =new CourseMapper() ;


    //manager
    public List<ManagerCourseDto> getAllCourses() {


        Iterable<Course>courses=courseDAO.findAll();


        if(courses.iterator().hasNext()){

            List <ManagerCourseDto> managerCourseDtos=new ArrayList<>( );

            courses.forEach(
                    course -> {

                        managerCourseDtos.add(courseMapper.returnManagerCourseDto(course));

                    }
            );


            return managerCourseDtos ;
        }


        throw new EntityNotFoundException("Course is empty ")  ;


    }

////formateur
//    public List<CourseDto> getFormateurCourses(Long formateur_id,Long formation_id){
//
//
//        Iterable<Course>courses = courseDAO.getFormateurCourses(formateur_id,formation_id) ;
//
//        if(courses.iterator().hasNext()){
//
//
//            List<CourseDto> courseDtos=new ArrayList<>();
//
//            courses.forEach(c->courseDtos.add(courseMapper.returnCourseDto(c)) );
//
//
//            return courseDtos ;
//
//
//        }
//
//
//        throw  new EntityNotFoundException("no course  found ") ;
//
//    }
//
//    //Student , manager
//    public Iterable<Course> findCoursesByStudentAndFormation(Long student_id,Long formation_id){
//
//        Iterable<Course> courses=this.courseDAO.findCoursesByStudentAndFormation(student_id,formation_id);
//        if(!courses.iterator().hasNext()) throw  new EntityNotFoundException("no course for student "+student_id);
//
//
//
//        return courses;
//
//
//    }



//formateur , manager , Student
    public Iterable<?> getFormationCourses(@Param("formation_id") Long formation_id, Principal connectedUser){


        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

return courseCacheService.getCachedFormationCourses(user,formation_id);

    }

    //manager
    public ManagerCourseDto getCourseById(Long id) {
        Optional<Course> optional = courseDAO.findById(id);

        Course course;
        if (optional.isPresent()) course = optional.get();
        else {

            throw new RuntimeException("Course not found for id  ::  " + id);


        }

        return courseMapper.returnManagerCourseDto(course);


    }


    //student , Trainer and manager
        public Object getCourseByName(String courseName, Principal connectedUser) {

            var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal() ;

return courseCacheService.getCachedCourse(user,courseName);

        }

    //manager
    @CacheEvict(cacheNames = "GetCourses",allEntries = true)
    public CourseDto  createNewCourse(CreateCourseDto createCourseDto) {

        courseValidator.validate(createCourseDto);

        Formation formation =formationDAO.findById(createCourseDto.getFormation_id())
                .orElseThrow(() -> new RuntimeException("Invalid formation id " ));

        Course course=courseMapper.returnCourse(createCourseDto,formation);
        courseDAO.save(course);
        log.info("course {} is saved", course.getCourse_id());

        return courseMapper.returnCourseDto(course);


    }



    //manager

    @Caching(evict = {
            @CacheEvict(cacheNames = "GetCourses",allEntries = true),
            @CacheEvict(cacheNames = "GetCourse", allEntries = true)
    })
    public String rem_Course(Long id) {

        courseDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("course not found with id: " + id));

        courseDAO.deleteById(id);
return "Course removed successfully";

    }




    @Caching(evict = {
            @CacheEvict(cacheNames = "GetCourses",allEntries = true),
            @CacheEvict(cacheNames = "GetCourse", allEntries = true)
    })
    //manager
    public CourseDto update_Course(CreateCourseDto updateCourseDto) {


courseValidator.validate(updateCourseDto);
        Optional<Course > course = courseDAO.findById(updateCourseDto.getInitiateCourseDto().getCourse_id()) ;

        if(course.isPresent()){


            Formation formation =formationDAO.findById(updateCourseDto.getFormation_id() )
                    .orElseThrow(() -> new RuntimeException("Invalid formation id " ));

            course.get().setCourse_description(updateCourseDto.getInitiateCourseDto().getCourse_description());
            course.get().setCourseName(updateCourseDto.getInitiateCourseDto().getCourseName());
            course.get().setFormation(formation);

            courseDAO.save(course.get());

            return courseMapper.returnCourseDto(course.get());

        }

        throw new EntityNotFoundException("payment not found for update ");



    }


    //manager

    @Caching(evict = {
            @CacheEvict(cacheNames = "GetCourses",allEntries = true),
            @CacheEvict(cacheNames = "GetCourse", allEntries = true)
    })
    public void updatepdf_url(Long course_id, String pdfUrl, String public_Id){

        Optional<Course> optionalCourse = courseDAO.findById(course_id) ;

        if (!optionalCourse.isPresent()){

            throw new IllegalArgumentException("course not found with ID: " + course_id);

        }

        Course course=optionalCourse.get() ;
        course.setCoursePdf_url(pdfUrl);
        course.setPdfPublicId(public_Id);

        courseDAO.save(course) ;


    }

    //manager

        public String  getVidPublicIdFromCourseData(Long course_id){

            String public_id= courseDAO.findById(course_id).get().getVidPublicId()  ;

            if (public_id!=null) return public_id ;


            throw new NullPointerException("public_id for the course not found  ") ;


        }
    public String  getPdfPublicIdFromCourseData(Long course_id){

        String public_id= courseDAO.findById(course_id).get().getPdfPublicId()  ;

        if (public_id!=null) return public_id ;


        throw new NullPointerException("public_id for the course not found  ") ;


    }

    @Caching(evict = {
            @CacheEvict(cacheNames = "GetCourses",allEntries = true),
            @CacheEvict(cacheNames = "GetCourse", allEntries = true)
    })
    //manager
    public void updatevideo_url(Long course_id,String videoUrl,String public_id){

        Optional<Course> OptionalCourse = courseDAO.findById(course_id) ;

        if (!OptionalCourse.isPresent()){

            throw new IllegalArgumentException("course not found with ID: " + course_id);


        }


        Course course=OptionalCourse.get() ;
        course.setCoursevideo_url(videoUrl);
        course.setVidPublicId(public_id);
        courseDAO.save(course) ;
    }


}
