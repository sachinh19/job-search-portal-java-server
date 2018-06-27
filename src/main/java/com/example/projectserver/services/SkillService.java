package com.example.projectserver.services;

import com.example.projectserver.models.Person;
import com.example.projectserver.models.Skill;
import com.example.projectserver.repositories.PersonRepository;
import com.example.projectserver.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "https://team2070.herokuapp.com", maxAge = 3600, allowCredentials = "true")
public class SkillService {

    private SkillRepository skillRepository;
    private PersonRepository personRepository;

    @Autowired
    SkillService(SkillRepository skillRepository, PersonRepository personRepository) {
        this.skillRepository = skillRepository;
        this.personRepository = personRepository;}

    @GetMapping("api/skill/{skillId}")
    public Skill findSkillById(@PathVariable("skillId") int skillId){
        return skillRepository.findById(skillId).orElse(null);
    }

    @DeleteMapping("api/skill/{skillId}")
    public void deleteSkill(@PathVariable("skillId") int skillId){
        Skill skill = skillRepository.findById(skillId).orElse(null);

        if(skill!= null){

            skillRepository.delete(skill);
        }
    }

    @PostMapping("api/person/{personId}/skill")
    public Skill createSkillForPerson(@RequestBody Skill newSkill, @PathVariable("personId") int personId){

        Person person = personRepository.findById(personId).orElse(null);

        if(person != null){
            newSkill.setPerson(person);
            return skillRepository.save(newSkill);
        }

        return null;
    }

    @PutMapping("api/skill/{skillId}")
    public Skill updateSkill(@PathVariable("skillId") int skillId, @RequestBody Skill newSkill) {

        Skill existingSkill = skillRepository.findById(skillId).orElse(null);

        if(existingSkill != null){

            String name = newSkill.getName();

            if(name != null){
                existingSkill.setName(name);
            }
            return skillRepository.save(existingSkill);
        }

        return null;
    }
}
