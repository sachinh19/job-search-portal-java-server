package com.example.projectserver.services;

import com.example.projectserver.models.Company;
import com.example.projectserver.models.Employer;
import com.example.projectserver.models.Person;
import com.example.projectserver.models.Role;
import com.example.projectserver.repositories.CompanyRepository;
import com.example.projectserver.repositories.EmployerRepository;
import com.example.projectserver.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
public class EmployerService {

    private EmployerRepository employerRepository;
    private RoleRepository roleRepository;
    private CompanyRepository companyRepository;

    @Autowired
    public EmployerService(EmployerRepository employerRepository, RoleRepository roleRepository, CompanyRepository companyRepository) {
        this.employerRepository = employerRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
    }


    @GetMapping("api/employer")
    public List<Employer> findAllEmployers() {
        return employerRepository.findAll();
    }

    @GetMapping("api/employer/{employerId}")
    public Employer findEmployerById(@PathVariable("employerId") int employerId) {

        return employerRepository.findById(employerId).orElse(null);
    }

    @GetMapping("api/employer/username/{username}")
    public Employer findEmployerByUsername(@PathVariable("username") String username) {
        return employerRepository.findEmployerByUsername(username).orElse(null);
    }

    @PostMapping("api/register/employer")
    public Employer createEmployer(@RequestBody Employer employer,HttpServletRequest request) {

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
        HttpSession session = request.getSession(true);
        session.setAttribute("currentUser", user);
        return user;
    }

    @PutMapping("api/employer")
    public Employer updateEmployer(@RequestBody Employer newEmployer,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        Employer employer = (Employer) session.getAttribute("currentUser");
        int employerId = employer.getId();
        Employer existingEmployer = employerRepository.findById(employerId).orElse(null);

        if (existingEmployer != null) {
            String username = newEmployer.getUsername();
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

            if (username != null) {
                existingEmployer.setUsername(username);
            }

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
            if (company != null) {

                existingEmployer.setCompany(company);
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
