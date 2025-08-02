package com.example.demo.mapper;

import com.example.demo.Dtos.courseDto.*;
import com.example.demo.entity.Course;
import com.example.demo.entity.Formation;

public class CourseMapper {


public UnpaidCourseDto returns_UnpaidCourseDto(Course course){




    return UnpaidCourseDto.builder()
            .initiateCourseDto(CreateInitiateCourse(course))
            .build() ;


}


public CourseDto returnCourseDto(Course course){


    return CourseDto.builder()
            .initiateCourseDto(CreateInitiateCourse(course))
            .coursePdf_url(course.getCoursePdf_url())
            .coursevideo_url(course.getCoursevideo_url())
            .build();


}


    public ManagerCourseDto returnManagerCourseDto(Course course){


        return ManagerCourseDto.builder()
                .initiateCourseDto(CreateInitiateCourse(course))
                .coursePdf_url(course.getCoursePdf_url())
                .coursevideo_url(course.getCoursevideo_url())
                .build();


    }





    public Course returnCourse(CreateCourseDto createCourseDto, Formation formation){



        return Course.builder()
                .courseName(createCourseDto.getInitiateCourseDto().getCourseName())
                .course_description(createCourseDto.getInitiateCourseDto().getCourse_description())
                .formation(formation)
                .course_id(createCourseDto.getInitiateCourseDto().getCourse_id())
                .build();





    }










    private InitiateCourseDto CreateInitiateCourse(Course course ){

        InitiateCourseDto initiateCourseDto =new InitiateCourseDto() ;

        initiateCourseDto.setCourse_id(course.getCourse_id());
        initiateCourseDto.setCourse_description(course.getCourse_description());
        initiateCourseDto.setCourseName(course.getCourseName());

        return initiateCourseDto;


    }
}
