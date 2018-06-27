package com.example.projectserver.services;

import com.example.projectserver.models.JobType;
import com.example.projectserver.repositories.JobTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://team2070.herokuapp.com", maxAge = 3600, allowCredentials = "true")
public class JobTypeService {

    private JobTypeRepository jobTypeRepository;

    @Autowired
    JobTypeService(JobTypeRepository jobTypeRepository) {
        this.jobTypeRepository = jobTypeRepository;
    }

    @GetMapping("api/jobtype")
    public List<JobType> findAllJobTypes() {
        return jobTypeRepository.findAll();
    }

    @GetMapping("api/jobtype/{jobtypeId}")
    public JobType findJobTypeById(@PathVariable("jobtypeId") int jobtypeId) {
        return jobTypeRepository.findById(jobtypeId).orElse(null);
    }

    @PostMapping("api/jobtype")
    public JobType createJobType(@RequestBody JobType newJobType) {
        return jobTypeRepository.save(newJobType);
    }

    @DeleteMapping("api/jobtype/{jobtypeId}")
    public void deleteJobTypeById(@PathVariable("jobtypeId") int jobtypeId) {
        JobType existingJobType = jobTypeRepository.findById(jobtypeId).orElse(null);

        if (existingJobType != null) {
            jobTypeRepository.delete(existingJobType);
        }
    }

    @PutMapping("api/jobtype/{jobtypeId}")
    public JobType updateJobType(@PathVariable("jobtypeId") int jobtypeId, @RequestBody JobType newJobType) {

        JobType existingJobType = jobTypeRepository.findById(jobtypeId).orElse(null);

        if (existingJobType != null) {
            String name = newJobType.getName();

            if (name != null) {
                existingJobType.setName(name);
            }
            return jobTypeRepository.save(existingJobType);
        }

        return existingJobType;
    }
}

