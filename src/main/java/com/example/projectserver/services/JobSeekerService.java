package com.example.projectserver.services;

import com.example.projectserver.models.JobSeeker;
import com.example.projectserver.models.Role;
import com.example.projectserver.repositories.JobSeekerRepository;
import com.example.projectserver.repositories.RoleRepository;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://team2070.herokuapp.com", maxAge = 3600, allowCredentials = "true")
public class JobSeekerService {

    private JobSeekerRepository jobSeekerRepository;
    private RoleRepository roleRepository;

    @Autowired
    JobSeekerService(JobSeekerRepository jobSeekerRepository, RoleRepository roleRepository) {
        this.jobSeekerRepository = jobSeekerRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("api/jobseeker")
    public List<JobSeeker> findAllJobSeekers() {
        return jobSeekerRepository.findAll();
    }

    @GetMapping("api/jobseeker/{jobseekerId}")
    public JobSeeker findJobSeekerById(@PathVariable("jobseekerId") int jobseekerId) {

        return jobSeekerRepository.findById(jobseekerId).orElse(null);
    }

    @GetMapping("api/jobseeker/username/{username}")
    public JobSeeker findJobSeekerByUsername(@PathVariable("username") String username) {
        return jobSeekerRepository.findJobSeekerByUsername(username).orElse(null);
    }

    @PostMapping("api/register/jobseeker")
    public JobSeeker createJobSeeker(@RequestBody JobSeeker jobSeeker) {
        jobSeeker.setRoleType("JobSeeker");
        Role role = roleRepository.findRoleByName("JobSeeker").orElse(null);
        jobSeeker.setRole(role);
        JobSeeker user = jobSeekerRepository.save(jobSeeker);
        return user;
    }

    @PutMapping("api/jobseeker/{username}")
    public JobSeeker updateJobSeeker(@RequestBody JobSeeker newJobSeeker,
                                     @PathVariable("username") String username,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {


        JobSeeker existingJobSeeker = jobSeekerRepository.findJobSeekerByUsername(username).orElse(null);

        if (existingJobSeeker != null) {
            String name = newJobSeeker.getUsername();
            String password = newJobSeeker.getPassword();
            String firstName = newJobSeeker.getFirstName();
            String lastName = newJobSeeker.getLastName();
            String email = newJobSeeker.getEmail();
            String interestedPosition = newJobSeeker.getInterestedPosition();
            String totalExp = newJobSeeker.getTotalExp();
            String expDescription = newJobSeeker.getExpDescription();
            String aboutMe = newJobSeeker.getAboutMe();

            if (password != null) {
                existingJobSeeker.setPassword(password);
            }
            if (firstName != null) {
                existingJobSeeker.setFirstName(firstName);
            }
            if (lastName != null) {
                existingJobSeeker.setLastName(lastName);
            }
            if (email != null) {
                existingJobSeeker.setEmail(email);
            }

            if (interestedPosition != null) {
                existingJobSeeker.setInterestedPosition(interestedPosition);
            }
            if (totalExp != null) {
                existingJobSeeker.setTotalExp(totalExp);
            }
            if (expDescription != null) {
                existingJobSeeker.setExpDescription(expDescription);
            }
            if (aboutMe != null) {
                existingJobSeeker.setAboutMe(aboutMe);
            }
            existingJobSeeker.setUpdated(new Date());
            return jobSeekerRepository.save(existingJobSeeker);
        }

        response.setStatus(204);
        return existingJobSeeker;
    }

}
