package com.example.demo.service;

import com.example.demo.entity.Enums.Role;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@Service
public class Student_ImageService {


    @Autowired
    CloudinaryService cloudinaryService;



    @Autowired
    private StudentService studentService ;



    //student
    public String upload_image( MultipartFile multipartFile ,
                                               Principal connectedUser)
             {

        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();


        if(user==null || !user.getRole().equals(Role.STUDENT)){
           throw new IllegalStateException("User Must be Student !!");
        }



        try {
            BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
            if (bi == null) {
                throw new RuntimeException("image is not valid ! ") ;
            }
            Map result = cloudinaryService.upload(multipartFile);



            studentService.updateStudentImage(user.getId(), (String) result.get("url"),(String) result.get("public_id") );

            return "image ajoutée avec succès !";
        } catch (IOException e) {

           return e.getMessage() ;

        }


    }



    public String delete_image(Principal connectedUser) {

        User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();


        String public_id = studentService.getPublicIdFromStudentData(user.getId()) ;
        try {
            cloudinaryService.delete(public_id);
        } catch (IOException e) {
            return  e.getMessage();
        }
        studentService.updateStudentImage(user.getId(), null,null);

return "Image deleted successfully";


    }


}
