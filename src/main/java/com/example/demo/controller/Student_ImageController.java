package com.example.demo.controller;
import com.example.demo.entity.User;
import com.example.demo.service.CloudinaryService;
import com.example.demo.service.StudentService;
import com.example.demo.service.Student_ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v6/images")
public class Student_ImageController {



  @Autowired
  private Student_ImageService studentImageService ;


    @PostMapping("/upload_image/{student_id}")
    public ResponseEntity<String> upload_image(@RequestParam("file") MultipartFile multipartFile ,
          Principal connectedUser)
            throws IOException {

      return new ResponseEntity<>(studentImageService.upload_image(multipartFile,connectedUser),HttpStatus.OK);

    }



    @DeleteMapping("/delete_image/{id}")
    public ResponseEntity<String> delete_image(Principal connectedUser) {


        return new ResponseEntity<>(studentImageService.delete_image(connectedUser), HttpStatus.OK);



    }





}