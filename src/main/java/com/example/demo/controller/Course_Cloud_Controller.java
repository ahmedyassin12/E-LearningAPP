package com.example.demo.controller;
import com.example.demo.service.CloudinaryService;
import com.example.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/courseCloud")

public class Course_Cloud_Controller {


    @Autowired
    CloudinaryService cloudinaryService;


    @Autowired
    private CourseService courseService;


    @PostMapping("/upload_pdf/{course_id}")
    public ResponseEntity<String> upload_pdf(@RequestParam("file") MultipartFile multipartFile ,@PathVariable ("course_id") Long course_id) throws IOException {

        if(courseService.getCourseById(course_id)==null)         return new ResponseEntity<>("there is no course with id   "+course_id, HttpStatus.BAD_REQUEST);

        Map result = cloudinaryService.upload(multipartFile);

        courseService.updatepdf_url(course_id, (String) result.get("url"), (String) result.get("public_id"));

        return new ResponseEntity<>("pdf uploaded with success !", HttpStatus.OK);

    }

    @PostMapping("/upload_video/{course_id}")
    public ResponseEntity<String> upload_video(@RequestParam("file")MultipartFile multipartFile , @PathVariable("course_id") Long course_id) throws IOException {

        if(courseService.getCourseById(course_id)==null)         return new ResponseEntity<>("there is no course with id   "+course_id, HttpStatus.BAD_REQUEST);

        Map result = cloudinaryService.upload(multipartFile);

        courseService.updatevideo_url(course_id,(String) result.get("url"),(String) result.get("public_id") );

        return new ResponseEntity<>("video uploaded successfully ! ", HttpStatus.OK);


    }

    @DeleteMapping("/delete_pdf/{course_is}")
    public ResponseEntity<String> delete_pdf(@PathVariable("course_is") Long course_is) {

        String public_id = courseService.getPublicIdFromCourseData(course_is) ;
        try {
            cloudinaryService.delete(public_id);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to delete pdf from Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        courseService.updatepdf_url(course_is,null,null);

        return new ResponseEntity<>("pdf deleted successfully  !", HttpStatus.OK);
    }
    @DeleteMapping("/delete_video/{course_is}")
    public ResponseEntity<String> delete_video(@PathVariable("course_is") Long course_is) {

        String public_id = courseService.getPublicIdFromCourseData(course_is) ;
        try {
            cloudinaryService.delete(public_id);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to delete pdf from Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        courseService.updatevideo_url(course_is,null,null);

        return new ResponseEntity<>("video deleted successfully  !", HttpStatus.OK);
    }


}
