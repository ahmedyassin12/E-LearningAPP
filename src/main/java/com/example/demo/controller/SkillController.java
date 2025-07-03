package com.example.demo.controller;

import com.example.demo.Dtos.skillDto.SkillDto;
import com.example.demo.entity.Skill;
import com.example.demo.service.SkillService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    @PostMapping("/addSkill")
    public ResponseEntity<SkillDto> addSkill(@RequestBody SkillDto skillDto) {


        return ResponseEntity.ok(skillService.createSkill(skillDto));


    }

    @GetMapping("/getAllSkills")
    public ResponseEntity<List<SkillDto>> getAllSkills() {
        return new ResponseEntity<>(skillService.getAllSkills(), HttpStatus.OK) ;
    }

    @GetMapping("/getSkillByID/{id}")
    public ResponseEntity<SkillDto> getSkillByID(@PathVariable Long id) {
        return new ResponseEntity<>(skillService.getSkillById(id),HttpStatus.OK);



    }


    @PutMapping("/updateSkill")
    public ResponseEntity<SkillDto> updateSkill(@RequestBody SkillDto skillDto){


        return new ResponseEntity<>(skillService.updateSkill(skillDto),HttpStatus.OK);


    }

    @DeleteMapping("/deleteSkill/{id}")
    public ResponseEntity<String> deleteSkill(@PathVariable  Long id ){

        return new ResponseEntity<>(skillService.deleteSkill(id),HttpStatus.OK);



    }

    @PostConstruct
    public void initializeSkills() {

        skillService.initializeSkills();


    }


    }
