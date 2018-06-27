package com.example.projectserver.services;

import com.example.projectserver.models.Award;
import com.example.projectserver.models.Person;
import com.example.projectserver.repositories.AwardRepository;
import com.example.projectserver.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@CrossOrigin(origins = "https://team2070.herokuapp.com", maxAge = 3600, allowCredentials = "true")
public class AwardService {

    private AwardRepository awardRepository;
    private PersonRepository personRepository;

    @Autowired
    AwardService(AwardRepository awardRepository, PersonRepository personRepository) {
        this.awardRepository = awardRepository;
        this.personRepository = personRepository;
    }

    @GetMapping("api/award/{awardId}")
    public Award findAwardById(@PathVariable("awardId") int awardId) {
        return awardRepository.findById(awardId).orElse(null);
    }

    @DeleteMapping("api/award/{awardId}")
    public void deleteAward(@PathVariable("awardId") int awardId) {
        Award award = awardRepository.findById(awardId).orElse(null);

        if (award != null) {

            awardRepository.delete(award);
        }
    }

    @PostMapping("api/person/{personId}/award")
    public Award createAwardForPerson(@RequestBody Award newAward, @PathVariable("personId") int personId) {

        Person person = personRepository.findById(personId).orElse(null);

        if (person != null) {
            newAward.setPerson(person);
            return awardRepository.save(newAward);
        }

        return null;
    }

    @PutMapping("api/award/{awardId}")
    public Award updateAward(@PathVariable("awardId") int awardId, @RequestBody Award newAward) {

        Award existingAward = awardRepository.findById(awardId).orElse(null);

        if(existingAward != null){

            String name = newAward.getName();
            Date date = newAward.getDate();

            if(name != null){
                existingAward.setName(name);
            }
            existingAward.setDate(date);
            return awardRepository.save(existingAward);
        }

        return null;
    }
}
