package com.example.demo.controller;

import com.example.demo.service.CloudinaryService;
import com.example.demo.service.FormationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

@RestController
public class Formation_imageController {

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    FormationService formationService ;

    @PostMapping("/upload_FormationImage/{formation_id}")
    public ResponseEntity<String> upload_FormationImage(@RequestParam("file") MultipartFile multipartFile , @PathVariable("formation_id") Long formation_id) throws IOException {
        if(formationService.getFormationById(formation_id)==null)         return new ResponseEntity<>("there is no formation with id   "+formation_id, HttpStatus.BAD_REQUEST);



        try {
            BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
            if (bi == null) {
                return new ResponseEntity<>("Image non valide!", HttpStatus.BAD_REQUEST);
            }
            Map result = cloudinaryService.upload(multipartFile);



            formationService.updateFormationImage(formation_id,(String) result.get("url"),(String) result.get("public_id") );
            System.out.println("public_id = "+result.get("public_id"));

            return new ResponseEntity<>("image ajoutée avec succès !", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Erreur de lecture du fichier image", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Erreur inattendue", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping("/delete_imageForFormation/{id}")
    public ResponseEntity<String> delete_imageForFormation(@PathVariable("id") Long id) {



        String public_id = formationService.getPublicIdFromFormationData(id) ;
        try {
            cloudinaryService.delete(public_id);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to delete image from Cloudinary", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        formationService.updateFormationImage(id,null,null);

        return new ResponseEntity<>("image supprimée !", HttpStatus.OK);



    }




}
