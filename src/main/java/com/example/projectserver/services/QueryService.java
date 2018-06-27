package com.example.projectserver.services;


import com.example.projectserver.models.Job;
import com.example.projectserver.models.JobQuery;
import com.example.projectserver.models.Person;
import com.example.projectserver.repositories.JobRepository;
import com.example.projectserver.repositories.PersonRepository;
import com.example.projectserver.repositories.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://team2070.herokuapp.com", maxAge = 3600, allowCredentials = "true")
public class QueryService {

    private QueryRepository queryRepository;
    private JobRepository jobRepository;
    private PersonRepository personRepository;

    @Autowired
    QueryService(QueryRepository queryRepository, JobRepository jobRepository, PersonRepository personRepository) {
        this.queryRepository = queryRepository;
        this.jobRepository = jobRepository;
        this.personRepository = personRepository;
    }

    @GetMapping("api/query")
    public List<JobQuery> findAllQueries() {
        return queryRepository.findAll();
    }

    @GetMapping("api/query/{queryId}")
    public JobQuery findQueryById(@PathVariable("queryId") int queryId) {
        return queryRepository.findById(queryId).orElse(null);
    }

    @GetMapping("api/job/{jobId}/query")
    public List<JobQuery> findAllQueriesForJob(@PathVariable("jobId") int jobId, HttpServletResponse response) {
        Job job = jobRepository.findById(jobId).orElse(null);
                if (job != null) {
            return job.getQueries();
        } else {
            response.setStatus(204);
            return null;
        }
    }


    @DeleteMapping("api/query/{queryId}")
    public void deleteQuery(@PathVariable("queryId") int queryId) {
        JobQuery jobQuery = queryRepository.findById(queryId).orElse(null);

        if (jobQuery != null) {
            queryRepository.delete(jobQuery);
        }
    }

    @PostMapping("api/job/{jobId}/query")
    public JobQuery createQuery(@PathVariable("jobId") int jobId,
                                @RequestBody JobQuery newJobQuery,
                                HttpSession session,
                                HttpServletResponse response) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job != null) {
            Person person = (Person) session.getAttribute("currentUser");
            Person existingperson = personRepository.findById(person.getId()).orElse(null);

            if (existingperson != null) {

                newJobQuery.setJob(job);
                newJobQuery.setPostedBy(existingperson);
                return queryRepository.save(newJobQuery);
            }

        }

        response.setStatus(204);
        return null;

    }

    @PutMapping("api/query/{queryId}")
    public JobQuery updateQuery(@PathVariable("queryId") int queryId, @RequestBody JobQuery newJobQuery, HttpServletResponse response) {

        JobQuery existingJobQuery = queryRepository.findById(queryId).orElse(null);

        if (existingJobQuery != null) {

            String post = newJobQuery.getPost();
            boolean status = newJobQuery.isStatus();
            Job job = newJobQuery.getJob();

            if (post != null) {
                existingJobQuery.setPost(post);
            }
            if (status != existingJobQuery.isStatus()) {
                existingJobQuery.setStatus(status);
            }
            if (job != null) {
                existingJobQuery.setJob(job);
            }

            return queryRepository.save(existingJobQuery);

        }
                return existingJobQuery;
    }
}
