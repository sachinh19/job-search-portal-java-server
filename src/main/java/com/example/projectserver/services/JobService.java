package com.example.projectserver.services;

import com.example.projectserver.models.Company;
import com.example.projectserver.models.Job;
import com.example.projectserver.models.JobType;
import com.example.projectserver.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class JobService {

    private JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;

    }

    @GetMapping("api/job")
    public List<Job> findAllJobs() {
        return jobRepository.findAll();
    }

    @GetMapping("api/job/{jobId}")
    public Job findJobById(@PathVariable("jobId") int jobId) {

        return jobRepository.findById(jobId).orElse(null);
    }

    @PostMapping("api/job")
    public Job createJob(@RequestBody Job job) {
        return jobRepository.save(job);
    }

    @DeleteMapping("api/job/{jobId}")
    public void deleteJob(@PathVariable("jobId") int jobId) {

        Job existingJob = jobRepository.findById(jobId).orElse(null);

        if (existingJob != null) {
            jobRepository.delete(existingJob);
        }
    }

    @PutMapping("api/job/{jobId}")
    public Job updateJob(@PathVariable("jobId") int jobId,
                         @RequestBody Job newJob,
                         HttpServletResponse response) {

        Job existingJob = jobRepository.findById(jobId).orElse(null);

        if (existingJob != null) {

            String position = newJob.getPosition();
            String city = newJob.getCity();
            String country = newJob.getCountry();
            String description = newJob.getDescription();
            String url = newJob.getUrl();
            int totalApplications = newJob.getTotalApplications();
            JobType jobType = newJob.getJobType();
            Company company = newJob.getCompany();

            if (position != null) {
                existingJob.setPosition(position);
            }
            if (city != null) {
                existingJob.setCity(city);
            }
            if (country != null) {
                existingJob.setCountry(country);
            }
            if (description != null) {
                existingJob.setDescription(description);
            }
            if (totalApplications > -1) {
                existingJob.setTotalApplications(totalApplications);
            }
            if (url != null) {
                existingJob.setUrl(url);
            }
            if (jobType != null) {
                existingJob.setJobType(jobType);
            }
            if (company != null) {
                existingJob.setCompany(company);
            }
            jobRepository.save(existingJob);
        }
        response.setStatus(204);
        return existingJob;
    }

}
