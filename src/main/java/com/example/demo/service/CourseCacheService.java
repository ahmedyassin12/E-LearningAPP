package com.example.demo.service;

import com.example.demo.Dtos.courseDto.CourseDto;
import com.example.demo.Dtos.courseDto.ManagerCourseDto;
import com.example.demo.Dtos.courseDto.UnpaidCourseDto;
import com.example.demo.dao.CourseDAO;
import com.example.demo.dao.EnrollementDAO;
import com.example.demo.dao.FormationDAO;
import com.example.demo.entity.Course;
import com.example.demo.entity.Enums.Role;
import com.example.demo.entity.Formation;
import com.example.demo.entity.User;
import com.example.demo.mapper.CourseMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseCacheService {
    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    EnrollementDAO enrollementDAO;
    @Autowired
    private FormationDAO formationDAO;


    CourseMapper courseMapper = new CourseMapper();

    @Cacheable(value = "GetCourses", key = "#user.id + '_' + #formation_id")
    public Iterable<?> getCachedFormationCourses(User user, Long formation_id) {

        System.out.println("Course From DB");

        Iterable<Course> courses = this.courseDAO.getFormationCourses(formation_id);

        if (courses.iterator().hasNext()) {


            Course course = courses.iterator().next();

            if (user.getRole().equals(Role.STUDENT)) {

                boolean isPaid = enrollementDAO.isEnrollmentPaid(user.getId(), course.getCourse_id());

                if (isPaid) {


                    List<CourseDto> courseDtos = new ArrayList<>();
                    courses.forEach(c -> courseDtos.add(courseMapper.returnCourseDto(c)));

                    return courseDtos;

                } else {
                    List<UnpaidCourseDto> unpaidDtos = new ArrayList<>();
                    courses.forEach(c -> unpaidDtos.add(courseMapper.returns_UnpaidCourseDto(c)));

                    return unpaidDtos;

                }

            } else if (user.getRole() == Role.MANAGER) {

                List<ManagerCourseDto> managerCourseDtos = new ArrayList<>();
                courses.forEach(c -> managerCourseDtos.add(courseMapper.returnManagerCourseDto(c)));

                return managerCourseDtos;

            } else if (user.getRole() == Role.FORMATEUR) {


                Formation formation = formationDAO.findFormationByNameForFormateur(course
                                        .getFormation().getFormationName()
                                , user.getId())
                        .orElseThrow(() -> new AccessDeniedException(
                                "Formateur not associated with formation: "
                                        + course.getFormation().getFormationName()
                        ));

                List<CourseDto> courseDtos = new ArrayList<>();
                courses.forEach(c -> courseDtos.add(courseMapper.returnCourseDto(c)));

                return courseDtos;


            } else {
                throw new AccessDeniedException("Role not authorized to access this resource");
            }

        }

        throw new EntityNotFoundException("no courses found");


    }

    @Cacheable(value = "GetCourse", key = "#user.id + '_' + #courseName")
    public Object getCachedCourse(User user, String courseName) {


        System.out.println("Course From DB");

        Optional<Course> optional = courseDAO.findCourseBycourseName(courseName);

        Course course;

        if (optional.isPresent()) {

            course = optional.get();

            if (user.getRole() == Role.STUDENT) {

                boolean isPaid = enrollementDAO.isEnrollmentPaid(user.getId(), course.getFormation().getId());

                if (isPaid) {
                    return courseMapper.returnCourseDto(course);
                } else {
                    UnpaidCourseDto courseDto = courseMapper.returns_UnpaidCourseDto(course);
                    return courseDto;

                }


            } else if (user.getRole().equals(Role.MANAGER)) {

                return courseMapper.returnManagerCourseDto(course);


            } else if (user.getRole() == Role.FORMATEUR) {


                Formation formation = formationDAO.findFormationByNameForFormateur(course
                                        .getFormation().getFormationName()
                                , user.getId())
                        .orElseThrow(() -> new AccessDeniedException(
                                "Formateur not associated with formation: "
                                        + course.getFormation().getFormationName()
                        ));

                return courseMapper.returnCourseDto(course);


            } else {
                throw new AccessDeniedException("Role not authorized to access this resource");
            }

        }


        throw new EntityNotFoundException("course not found for name  ::  " + courseName);


    }


}