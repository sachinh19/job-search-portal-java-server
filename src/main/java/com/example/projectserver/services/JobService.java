package com.example.projectserver.services;

import com.example.projectserver.models.*;
import com.example.projectserver.repositories.CompanyRepository;
import com.example.projectserver.repositories.JobRepository;
import com.example.projectserver.repositories.JobTypeRepository;
import com.example.projectserver.repositories.PersonRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@CrossOrigin(origins = "https://team2070.herokuapp.com", maxAge = 3600, allowCredentials = "true")
public class JobService {

    private static final String API_URL = "https://authenticjobs.com/api/?api_key=fbf2b1502bc1ccf4aac2d014afb4ad28&method=aj.jobs.search&format=json&perpage=60";
    private JobRepository jobRepository;
    private CompanyRepository companyRepository;
    private JobTypeRepository jobTypeRepository;
    private PersonRepository personRepository;

    @Autowired
    public JobService(JobRepository jobRepository, CompanyRepository companyRepository,
                      JobTypeRepository jobTypeRepository, PersonRepository personRepository) {
        this.jobRepository = jobRepository;
        this.companyRepository = companyRepository;
        this.jobTypeRepository = jobTypeRepository;
        this.personRepository = personRepository;
    }

    @GetMapping("api/job")
    public List<Job> findAllJobs() {
        return jobRepository.findAll();
    }

    @GetMapping("api/job/{jobId}")
    public Job findJobById(@PathVariable("jobId") int jobId, HttpServletResponse response) {
                return jobRepository.findById(jobId).orElse(null);
    }


    @GetMapping("api/searchJob/{searchText}")
    public List<Job> searchJobsByKeyword(@PathVariable("searchText") String searchText) {

        System.out.println("searchJobsByKeyword" + searchText);
        List<Job> jobs = new ArrayList<Job>();
        Set<Job> jobsSet = new HashSet<Job>();

        String[] keywords = searchText.split("\\+\\+");
        for(String s:keywords){
            jobsSet.addAll(jobRepository.findJobsByKeyword(s));
        }
        jobs.addAll(jobsSet);
        return jobs;
    }

    @PostMapping("api/job/userdefined")
    public Job createUserDefinedJob(@RequestBody Job job, HttpServletResponse response) {
        Optional<Company> result = companyRepository.findCompanyByName(job.getCompany().getName());
        if(result.isPresent()) {
            Company company = result.get();
            job.setCompany(company);
        }
        Optional<JobType> result1 = jobTypeRepository.findJobTypeByName(job.getJobType().getName());
        if(result1.isPresent()) {
            JobType jobType = result1.get();
            job.setJobType(jobType);
        }

        return jobRepository.save(job);
    }


    @PostMapping("api/job")
    public Job createJob(@RequestBody Job job, HttpServletResponse response) {
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
    public void deleteJob(@PathVariable("jobId") int jobId, HttpServletResponse response) {

        Job existingJob = jobRepository.findById(jobId).orElse(null);

        if (existingJob != null) {
            jobRepository.delete(existingJob);
        }
            }

    @PutMapping("api/job/{jobId}/addapplicant")
    public Job addApplicant(@PathVariable("jobId") int jobId, HttpServletResponse response,
                            HttpSession session) {
        Job existingJob = jobRepository.findById(jobId).orElse(null);
        if (existingJob != null) {
            Person person = (Person) session.getAttribute("currentUser");
            Person existingperson = personRepository.findById(person.getId()).orElse(null);
            List<Person> applicants = existingJob.getPersons();
            int totalApplicants = existingJob.getTotalApplications();
            if (existingperson != null && !applicants.contains(existingperson)) {
                applicants.add(existingperson);
                existingJob.setPersons(applicants);
                totalApplicants = totalApplicants + 1;
                existingJob.setTotalApplications(totalApplicants);
                return jobRepository.save(existingJob);
            }
        }

        response.setStatus(204);
        return null;
    }


    @GetMapping("api/job/{jobId}/status")
    public void getApplicationStatus(@PathVariable("jobId") int jobId, HttpServletResponse response, HttpSession session) {
        Job existingJob = jobRepository.findById(jobId).orElse(null);
        if (existingJob != null) {
            Person person = (Person) session.getAttribute("currentUser");
            Person existingperson = personRepository.findById(person.getId()).orElse(null);
            List<Person> applicants = existingJob.getPersons();
            if (existingperson != null) {
               if (applicants.contains(existingperson)) {
                   response.setStatus(200);
               } else {
                   response.setStatus(204);
               }
            } else {
                response.setStatus(204);
            }
        } else {
            response.setStatus(204);
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
            String keywords = newJob.getKeywords();
            int totalApplications = newJob.getTotalApplications();
            JobType jobType = newJob.getJobType();
            Company company = newJob.getCompany();

            if (position != null) {
                existingJob.setPosition(position);
            }
            if(keywords!=null) {
                existingJob.setKeywords(keywords);
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
            response.setStatus(200);
        }
        else
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

            if (newJob.has("keywords")) {

                job.setKeywords(newJob.get("keywords").textValue());
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
