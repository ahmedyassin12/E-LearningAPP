package com.example.demo.mapper;

import com.example.demo.Dtos.skillDto.SkillDto;
import com.example.demo.entity.Skill;

public class SkillMapper {



    public SkillDto  returnSkillDto(Skill skill){


        return SkillDto.builder()
                .id(skill.getId())
                .name(skill.getName())
                .build();

    }

    public Skill returnSkill(SkillDto skillDto ){

        return Skill .builder()
                .name(skillDto.getName())
                .build();

    }
}
