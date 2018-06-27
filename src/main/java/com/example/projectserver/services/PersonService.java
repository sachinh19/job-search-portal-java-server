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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "https://team2070.herokuapp.com", maxAge = 3600, allowCredentials = "true")
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
    public List<Person> findAllPersons(HttpServletResponse response) {
        return personRepository.findAll();
    }

    @GetMapping("api/person/job/{username}")
    public List<Job> findAllJobsForPerson(@PathVariable("username") String username, HttpServletResponse response) {

        Optional<Person> result = personRepository.findPersonByUsername(username);
        if (result.isPresent()) {
            Person existingPerson = result.get();
            int personId = existingPerson.getId();
            Person person = personRepository.findById(personId).orElse(null);
            List<Job> jobs = person.getJobs();
            response.setStatus(200);
            return jobs;
        }
        response.setStatus(204);
        return null;
    }

    @GetMapping("api/person/company/job/{username}")
    public List<Job> findJobsForPersonByCompany(@PathVariable("username") String username, HttpServletResponse response) {
        Company company = companyRepository.findCompanyByName("defaultCompany").orElse(null);
        List<Job> jobs = jobRepository.findJobsByCompany(company);
        return jobs;
    }


    @GetMapping("api/person/query")
    public List<JobQuery> findAllQueriesForPerson(HttpServletRequest request, HttpServletResponse response) {
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
            session.setAttribute("currentUser", existingPerson);
            response.setStatus(200);
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


    @PutMapping("/api/follow")
    public void followUser(@RequestBody Person followUserObject,
                           HttpSession session,
                           HttpServletResponse response) {

        String followUsername = followUserObject.getUsername();
        Person toFollowUser = personRepository.findPersonByUsername(followUsername).orElse(null);
        Person followedByUser = (Person) session.getAttribute("currentUser");

        if (toFollowUser != null && followedByUser != null) {
            List<Person> followingUsers = followedByUser.getFollowing();
            followingUsers.add(toFollowUser);
            followedByUser.setFollowing(followingUsers);
            personRepository.save(followedByUser);
        } else {
            response.setStatus(204);
        }
    }

    @PutMapping("/api/unfollow")
    public void unfollowUser(@RequestBody Person unfollowUserObject,
                           HttpSession session,
                           HttpServletResponse response) {

        String unfollowUsername = unfollowUserObject.getUsername();
        Person followedByUser = (Person) session.getAttribute("currentUser");

        if (followedByUser != null) {
            List<Person> followingUsers = followedByUser.getFollowing();
            followingUsers = followingUsers.stream().filter(followingUser -> !unfollowUsername.equals(followingUser.getUsername())).collect(Collectors.toList());
            followedByUser.setFollowing(followingUsers);
            personRepository.save(followedByUser);
        } else {
            response.setStatus(204);
        }
    }

    @GetMapping("/api/following")
    public List<Person> isFollowingUser(HttpSession session, HttpServletResponse response) {
        Person person = (Person) session.getAttribute("currentUser");

        if (person != null) {
            List<Person> followingList = person.getFollowing();
            return  followingList;

        } else {
            response.setStatus(204);
            return null;
        }

    }
    @GetMapping("/api/followers/{username}")
    public List<Person> getFollowedBy (@PathVariable("username") String username, HttpSession session, HttpServletResponse response) {
        Person person = personRepository.findPersonByUsername(username).orElse(null);

        if (person != null) {
            List<Person> followedByList = person.getFollowedBy();
            return  followedByList;

        } else {
            response.setStatus(204);
            return null;
        }
    }

    @GetMapping("/api/following/{username}")
    public List<Person> getFollowing (@PathVariable("username") String username, HttpSession session, HttpServletResponse response) {
        Person person = personRepository.findPersonByUsername(username).orElse(null);

        if (person != null) {
            List<Person> followingList = person.getFollowing();
            return  followingList;

        } else {
            response.setStatus(204);
            return null;
        }
    }
}