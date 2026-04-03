package com.naukri.database_api.controllers;

import main.com.naukri.database_api.models.Skill;
import com.naukri.database_api.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/db/skill")
public class SkillController {

    SkillRepository skillRepository;

    @Autowired
    public SkillController(SkillRepository skillRepository){
        this.skillRepository= skillRepository;
    }

    @PostMapping("/save")
    public ResponseEntity saveSkill(@RequestBody Skill skill){
        Skill skills = skillRepository.save(skill);
        return new ResponseEntity(skills, HttpStatus.CREATED);
    }

    @GetMapping("/get/{skillName}")
    public ResponseEntity getSkillByName(@PathVariable String skillName){
        Skill skill = skillRepository.findByName(skillName);
        return new ResponseEntity(skill, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getSkillById(@PathVariable UUID id){
        Skill skill = skillRepository.findById(id).orElse(null);
        return new ResponseEntity(skill, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Skill>> getAllSkills(){
        List<Skill> skills = skillRepository.findAll();
        return new ResponseEntity(skills, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity updateSkill(@RequestBody Skill skill){
        Skill newskill = skillRepository.save(skill);
        return new ResponseEntity(newskill,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable UUID id){
        skillRepository.deleteById(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
