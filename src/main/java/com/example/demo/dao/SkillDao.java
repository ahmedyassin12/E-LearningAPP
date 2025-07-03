package com.example.demo.dao;

import com.example.demo.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillDao extends JpaRepository<Skill, Long> {

    Optional<Skill> findByName(String name);


    boolean existsByNameIgnoreCase(String name);

    Long id(Long id);
}
