package com.example.projectserver.services;

import com.example.projectserver.models.Person;
import com.example.projectserver.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class PersonService {

    private PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    @GetMapping("api/person/{personId}")
    public Person findPersonById(@PathVariable("personId")int personId){

        return personRepository.findById(personId).orElse(null);
    }

    @GetMapping("api/person")
    public List<Person> findAllPersons(){

        return personRepository.findAll();
    }

    @PostMapping("api/login")
    public Person login(@RequestBody Person person, HttpServletRequest request, HttpServletResponse response){

        Person existingPerson = personRepository.findPersonByCredentials(person.getUsername(), person.getPassword()).orElse(null);

        if(existingPerson != null){
            HttpSession session = request.getSession(true);
            session.setAttribute("currentUser", existingPerson);
            return existingPerson;
        }

        response.setStatus(204);
        return null;
    }

    @DeleteMapping("api/person/{personId}")
    public void deletePerson(@PathVariable("personId") int personId){

        Person existingPerson = personRepository.findById(personId).orElse(null);

        if(existingPerson != null){

            personRepository.delete(existingPerson);
        }
    }
}
