package com.example.projectserver.services;

import com.example.projectserver.models.*;
import com.example.projectserver.repositories.CompanyRepository;
import com.example.projectserver.repositories.EmployerRepository;
import com.example.projectserver.repositories.JobRepository;
import com.example.projectserver.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "https://team2070.herokuapp.com", maxAge = 3600, allowCredentials = "true")
public class EmployerService {

    private EmployerRepository employerRepository;
    private RoleRepository roleRepository;
    private CompanyRepository companyRepository;
    private JobRepository jobRepository;

    @Autowired
    public EmployerService(JobRepository jobRepository, EmployerRepository employerRepository, RoleRepository roleRepository, CompanyRepository companyRepository) {
        this.employerRepository = employerRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.jobRepository = jobRepository;
    }


    @GetMapping("api/employer")
    public List<Employer> findAllEmployers() {
        return employerRepository.findAll();
    }

    @GetMapping("api/employer/{employerId}")
    public Employer findEmployerById(@PathVariable("employerId") int employerId) {

        return employerRepository.findById(employerId).orElse(null);
    }

    @GetMapping("api/employer/job/{username}")
    public List<Job> findJobsForEmployer(@PathVariable("username") String username, HttpServletResponse response)    {
        Optional<Employer> result = employerRepository.findEmployerByUsername(username);
        if(result.isPresent()){
            Employer existingEmployer = result.get();
            String companyName = existingEmployer.getCompanyName();
            Company company = companyRepository.findCompanyByName(companyName).orElse(null);
            List<Job> jobs = jobRepository.findJobsByCompany(company);
            response.setStatus(200);
            return jobs;
        }
        response.setStatus(204);
        return null;
    }

    @GetMapping("api/employer/username/{username}")
    public Employer findEmployerByUsername(@PathVariable("username") String username) {
        return employerRepository.findEmployerByUsername(username).orElse(null);
    }

    @PostMapping("api/register/employer")
    public Employer createEmployer(@RequestBody Employer employer) {

        employer.setRoleType("Employer");
        Role role = roleRepository.findRoleByName("Employer").orElse(null);

        String companyName = employer.getCompanyName();
        Company company = companyRepository.findCompanyByName(companyName).orElse(null);

        if (company == null) {
            Company newCompany = new Company();
            newCompany.setName(companyName);
            company = companyRepository.save(newCompany);
        }
        employer.setCompany(company);
        employer.setRole(role);
        Employer user = employerRepository.save(employer);
        return user;
    }

    @PutMapping("api/employer/{username}")
    public Employer updateEmployer(@RequestBody Employer newEmployer,
                                   @PathVariable("username") String username,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        Employer existingEmployer = employerRepository.findEmployerByUsername(username).orElse(null);

        if (existingEmployer != null) {
            String name = newEmployer.getUsername();
            String password = newEmployer.getPassword();
            String firstName = newEmployer.getFirstName();
            String lastName = newEmployer.getLastName();
            String email = newEmployer.getEmail();
            String position = newEmployer.getPosition();
            String tenure = newEmployer.getTenure();
            String companyName = newEmployer.getCompanyName();
            Company company = companyRepository.findCompanyByName(companyName).orElse(null);
            String expDescription = newEmployer.getExpDescription();
            String aboutMe = newEmployer.getAboutMe();

            if (password != null) {
                existingEmployer.setPassword(password);
            }
            if (firstName != null) {
                existingEmployer.setFirstName(firstName);
            }
            if (lastName != null) {
                existingEmployer.setLastName(lastName);
            }
            if (email != null) {
                existingEmployer.setEmail(email);
            }

            if (position != null) {
                existingEmployer.setPosition(position);
            }
            if (tenure != null) {
                existingEmployer.setTenure(tenure);
            }
            if (company == null && companyName != null) {
                Company newCompany = new Company();
                newCompany.setName(companyName);
                newCompany = companyRepository.save(newCompany);
                existingEmployer.setCompany(newCompany);

            } else if (company != null) {
                existingEmployer.setCompany(company);
            }



            if (companyName != null) {
                existingEmployer.setCompanyName(companyName);
            }
            if (expDescription != null) {
                existingEmployer.setExpDescription(expDescription);
            }
            if (aboutMe != null) {
                existingEmployer.setAboutMe(aboutMe);
            }
            existingEmployer.setUpdated(new Date());
            return employerRepository.save(existingEmployer);
        }
        response.setStatus(204);
        return existingEmployer;
    }
}
