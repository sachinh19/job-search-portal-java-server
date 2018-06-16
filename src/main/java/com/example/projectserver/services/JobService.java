package com.example.projectserver.services;

import com.example.projectserver.models.Company;
import com.example.projectserver.models.Job;
import com.example.projectserver.models.JobType;
import com.example.projectserver.repositories.CompanyRepository;
import com.example.projectserver.repositories.JobRepository;
import com.example.projectserver.repositories.JobTypeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class JobService {

    private static final String API_URL = "https://authenticjobs.com/api/?api_key=fbf2b1502bc1ccf4aac2d014afb4ad28&method=aj.jobs.search&format=json&perpage=60";

    private JobRepository jobRepository;
    private CompanyRepository companyRepository;
    private JobTypeRepository jobTypeRepository;

    @Autowired
    public JobService(JobRepository jobRepository, CompanyRepository companyRepository, JobTypeRepository jobTypeRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.jobTypeRepository = jobTypeRepository;
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
    public Job createJob(@RequestBody Job job, HttpServletResponse response) {

        System.out.println(job.getApiId());
        Job existingJob = jobRepository.findJobByApiId(job.getApiId()).orElse(null);
        if(existingJob!=null){
            return updateJob(existingJob.getId(),job, response);
        }else{

            Company company = companyRepository.findCompanyByApiId(job.getCompany().getApiId()).orElse(null);
            JobType jobType = jobTypeRepository.findJobTypeByName(job.getJobType().getName()).orElse(null);

            if (!(company != null)) {
                company = companyRepository.save(job.getCompany());
            }

            if (!(jobType != null)) {
                jobType = jobTypeRepository.save(job.getJobType());
            }

            job.setCompany(company);
            job.setJobType(jobType);
            return jobRepository.save(job);
        }
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

            jobRepository.save(existingJob);
        }
        response.setStatus(204);
        return existingJob;
    }

    @GetMapping("api/getjobs")
    public List<Job> getNewJobs(HttpServletResponse response) throws IOException, ParseException {

        JsonNode jsonNode = getDatafromUrl();

        List<Job> jobList = new ArrayList<Job>();

        Iterator<JsonNode> itr = jsonNode.get("listings").get("listing").elements();

        while (itr.hasNext()) {

            Job job = new Job();
            Company company = new Company();
            JobType jobType = new JobType();

            JsonNode newJob = itr.next();

            if (newJob.has("company")) {

                company.setApiId(newJob.get("company").get("id").textValue());
                company.setUrl(newJob.get("company").get("url").textValue());
                company.setName(newJob.get("company").get("name").textValue());
                if(newJob.get("company").has("location"))
                    company.setState(newJob.get("company").get("location").get("name").textValue());
            }

            job.setCompany(company);

            if (newJob.has("type")) {

                jobType.setName(newJob.get("type").get("name").textValue());
            }

            job.setJobType(jobType);

            if (newJob.has("title")) {

                job.setPosition(newJob.get("title").textValue());
            }
            if (newJob.has("description")) {

                job.setDescription(newJob.get("description").textValue());
            }

            if (newJob.has("id")) {

                job.setApiId(newJob.get("id").textValue());
            }

            if (newJob.has("url")) {

                job.setUrl(newJob.get("url").textValue());
            }

            if (newJob.has("post_date")) {

                job.setPostedDate(new SimpleDateFormat("yyyy-MM-dd").parse(newJob.get("post_date").textValue().split(" ")[0]));
            }

            jobList.add(createJob(job, response));
        }

        return jobList;

    }


    public static JsonNode getDatafromUrl() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String s = (String)restTemplate.getForObject(API_URL, String.class);
        JsonNode jsonNode = new ObjectMapper().readTree(s);
        return jsonNode;
    }

}
