package com.example.projectserver.services;

import com.example.projectserver.models.JobSeeker;
import com.example.projectserver.models.Role;
import com.example.projectserver.repositories.JobSeekerRepository;
import com.example.projectserver.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
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

    @PostMapping("api/register/jobseeker")
    public JobSeeker createJobSeeker(@RequestBody JobSeeker jobSeeker) {

        jobSeeker.setRoleType("JobSeeker");
        Role role = roleRepository.findRoleByName("JobSeeker").orElse(null);
        jobSeeker.setRole(role);
        return jobSeekerRepository.save(jobSeeker);
    }

    @PutMapping("api/jobseeker/{jobseekerId}")
    public JobSeeker updateJobSeeker(@PathVariable("jobseekerId") int jobseekerId,
                                     @RequestBody JobSeeker newJobSeeker,
                                     HttpServletResponse response) {

        JobSeeker existingJobSeeker = jobSeekerRepository.findById(jobseekerId).orElse(null);

        if (existingJobSeeker != null) {
            String username = newJobSeeker.getUsername();
            String password = newJobSeeker.getPassword();
            String firstName = newJobSeeker.getFirstName();
            String lastName = newJobSeeker.getLastName();
            String email = newJobSeeker.getEmail();
            String interestedPosition = newJobSeeker.getInterestedPosition();
            String totalExp = newJobSeeker.getTotalExp();
            String expDescription = newJobSeeker.getExpDescription();
            String aboutMe = newJobSeeker.getAboutMe();


            if (username != null) {
                existingJobSeeker.setUsername(username);
            }

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

            return jobSeekerRepository.save(existingJobSeeker);
        }

        response.setStatus(204);
        return existingJobSeeker;
    }

}
