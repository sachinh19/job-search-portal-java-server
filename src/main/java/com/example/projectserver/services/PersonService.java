package com.example.projectserver.services;

import com.example.projectserver.models.*;
import com.example.projectserver.repositories.CompanyRepository;
import com.example.projectserver.repositories.JobRepository;
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
    private CompanyRepository companyRepository;
    private JobRepository jobRepository;


    @Autowired
    public PersonService(PersonRepository personRepository,
                         CompanyRepository companyRepository,
                         JobRepository jobRepository){
        this.personRepository = personRepository;
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
    }

    @GetMapping("api/person/{personId}")
    public Person findPersonById(@PathVariable("personId") int personId, HttpServletResponse response) {
        Person person = personRepository.findById(personId).orElse(null);
        if (person != null) {
            return person;
        } else {
            response.setStatus(204);
            return null;
        }

    }

    @GetMapping("api/person/currentuser")
    public Person findCurrentUser(HttpSession session, HttpServletResponse response) {
        Person currentUser = (Person) session.getAttribute("currentUser");

        if (currentUser != null) {
            return currentUser;
        } else {
            response.setStatus(204);
            return null;
        }
    }

    @GetMapping("api/person/username/{username}")
    public Person findPersonByUsername(@PathVariable("username") String username, HttpServletResponse response) {
        Person existingPerson = personRepository.findPersonByUsername(username).orElse(null);
        if (existingPerson != null)
            return existingPerson;
        response.setStatus(204);
        return null;
    }

    @GetMapping("api/person")
    public List<Person> findAllPersons(HttpServletResponse response)    {
        return personRepository.findAll();
    }

    @GetMapping("api/person/job")
    public List<Job> findAllJobsForPerson(HttpServletRequest request, HttpServletResponse response)    {
        HttpSession session = request.getSession(false);
        Person existingPerson = (Person) session.getAttribute("currentUser");
        int personId = existingPerson.getId();
        Person person = personRepository.findById(personId).orElse(null);
        List<Job> jobs = person.getJobs();
        return jobs;
    }

    @GetMapping("api/person/company/job")
    public List<Job> findJobsForPersonByCompany(HttpServletRequest request, HttpServletResponse response)    {
        HttpSession session = request.getSession(false);
        Company company = companyRepository.findCompanyByName("defaultCompany").orElse(null);
        List<Job> jobs = jobRepository.findJobsByCompany(company);
        return jobs;
    }


    @GetMapping("api/person/query")
    public List<JobQuery> findAllQueriesForPerson(HttpServletRequest request, HttpServletResponse response)    {
        HttpSession session = request.getSession(false);
        Person existingPerson = (Person) session.getAttribute("currentUser");
        int personId = existingPerson.getId();
        Person person = personRepository.findById(personId).orElse(null);
        return person.getQueries();
    }

    @PostMapping("api/login")
    public Person login(@RequestBody Person person, HttpServletRequest request, HttpServletResponse response) {
        Person existingPerson = personRepository.findPersonByCredentials(person.getUsername(), person.getPassword()).orElse(null);
        if (existingPerson != null) {
            HttpSession session = request.getSession(true);
            response.setStatus(200);
            session.setAttribute("currentUser", existingPerson);
            return existingPerson;
        }

        response.setStatus(204);
        return null;
    }

    @GetMapping("api/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @DeleteMapping("api/person/{personId}")
    public void deletePerson(@PathVariable("personId") int personId) {

        Person existingPerson = personRepository.findById(personId).orElse(null);
        if (existingPerson != null) {
            personRepository.delete(existingPerson);
        }
    }
}
