package com.example.demo.service;
import com.example.demo.Dtos.skillDto.SkillDto;
import com.example.demo.dao.SkillDao;
import com.example.demo.entity.Skill;
import com.example.demo.mapper.SkillMapper;
import com.example.demo.validator.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Service
public class SkillService {

    @Autowired
    private SkillDao skillRepository;


    private SkillMapper skillMapper =new SkillMapper();

    public SkillDto createSkill(SkillDto skillDto) {


        if (skillRepository.existsByNameIgnoreCase(skillDto.getName())) throw new IllegalArgumentException("Skill already exists !");


if(skillDto.getName().isEmpty() ) throw new IllegalArgumentException("Skill name cannot be empty ! ");

Skill saveSkill =skillMapper.returnSkill(skillDto);
         skillRepository.save(saveSkill);

         return skillDto;

    }


    public List<SkillDto> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();

        List<SkillDto> skillDtos =new ArrayList<>();

        for(Skill skill:skills){
            skillDtos.add(skillMapper.returnSkillDto(skill));
        }

        return skillDtos;


    }


    public SkillDto getSkillById(Long id) {

        Skill skill = skillRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("skill not available ! "));

        return skillMapper.returnSkillDto(skill);


    }

    public SkillDto updateSkill(SkillDto newSkillDto) {


        Skill existing = skillRepository.findById(newSkillDto.getId()).orElseThrow(() -> new EntityNotFoundException("skill not available ! "));
        if(newSkillDto.getName().isEmpty() ) throw new IllegalArgumentException("Skill name cannot be empty ! ");

        existing.setName(newSkillDto.getName());

         skillRepository.save(existing);

         return skillMapper.returnSkillDto(existing);

    }

    public String deleteSkill(Long id) {
        if (!skillRepository.existsById(id)) {
            throw new EntityNotFoundException("Skill not found");
        }
        skillRepository.deleteById(id);
        return "Skill deleted successfully";

    }


    public void initializeSkills() {
         List<String> DEFAULT_SKILLS = Arrays.asList(
                "Java", "Spring Boot", "Hibernate", "REST API", "Microservices",
                "Docker", "Kubernetes", "AWS", "SQL", "React", "Angular", "JavaScript",
                 "dataAnalysis","contentCreation"
        );

        for( String skillName :DEFAULT_SKILLS)

        {
            // Check if skill already exists
            if (!skillRepository.existsByNameIgnoreCase(skillName)) {
                Skill skill = new Skill();
                skill.setName(skillName);
                skillRepository.save(skill);
            }
        }
    }



}