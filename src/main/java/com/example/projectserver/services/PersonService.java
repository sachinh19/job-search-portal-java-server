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
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
public class PersonService {

    private PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    @GetMapping("api/person/{personId}")
    public Person findPersonById(@PathVariable("personId")int personId,HttpServletResponse response) {
        Person person = personRepository.findById(personId).orElse(null);
        if(person!=null)
            return person;
        response.setStatus(204);
        return null;
    }

    @GetMapping("api/person/username/{username}")
    public Person findPersonByUsername(@PathVariable("username")String username,HttpServletResponse response) {
                Person existingPerson = personRepository.findPersonByUsername(username).orElse(null);
        if(existingPerson!=null)
            return existingPerson;
        response.setStatus(204);
        return null;
    }

    @GetMapping("api/person")
    public List<Person> findAllPersons(HttpServletResponse response)    {
                return personRepository.findAll();
    }

    @PostMapping("api/login")
    public Person login(@RequestBody Person person, HttpServletRequest request, HttpServletResponse response){
                Person existingPerson = personRepository.findPersonByCredentials(person.getUsername(), person.getPassword()).orElse(null);
        if(existingPerson != null){
            HttpSession session = request.getSession(true);
            response.setStatus(200);
            session.setAttribute("currentUser", existingPerson);
            return existingPerson;
        }

        response.setStatus(204);
        return null;
    }

    @GetMapping("api/logout")
    public void logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
            }

    @DeleteMapping("api/person/{personId}")
    public void deletePerson(@PathVariable("personId") int personId, HttpServletResponse response){

        Person existingPerson = personRepository.findById(personId).orElse(null);
        if(existingPerson != null){
            personRepository.delete(existingPerson);
        }
            }
}
