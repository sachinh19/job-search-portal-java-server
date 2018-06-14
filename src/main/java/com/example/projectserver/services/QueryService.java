package com.example.projectserver.services;


import com.example.projectserver.models.Job;
import com.example.projectserver.models.Query;
import com.example.projectserver.repositories.JobRepository;
import com.example.projectserver.repositories.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class QueryService {

    private QueryRepository queryRepository;
    private JobRepository jobRepository;

    @Autowired
    QueryService(QueryRepository queryRepository, JobRepository jobRepository) {
        this.queryRepository = queryRepository;
        this.jobRepository = jobRepository;
    }

    @GetMapping("api/query")
    public List<Query> findAllQueries() {
        return queryRepository.findAll();
    }

    @GetMapping("api/query/{queryId}")
    public Query findQueryById(@PathVariable("queryId") int queryId) {
        return queryRepository.findById(queryId).orElse(null);
    }

    @DeleteMapping("api/query/{queryId}")
    public void deleteQuery(@PathVariable("queryId") int queryId) {
        Query query = queryRepository.findById(queryId).orElse(null);

        if (query != null) {
            queryRepository.delete(query);
        }
    }

    @PostMapping("api/job/{jobId}/query")
    public Query createQuery(@PathVariable("jobId") int jobId, @RequestBody Query newQuery) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if(job != null){
            newQuery.setJob(job);
        }
        return queryRepository.save(newQuery);
    }

    @PutMapping("api/query/{queryId}")
    public Query updateQuery(@PathVariable("queryId") int queryId, @RequestBody Query newQuery) {

        Query existingQuery = queryRepository.findById(queryId).orElse(null);

        if (existingQuery != null) {

            String post = newQuery.getPost();
            boolean status = newQuery.isStatus();
            Job job = newQuery.getJob();

            if(post != null){
                existingQuery.setPost(post);
            }
            if(status != existingQuery.isStatus()){
                existingQuery.setStatus(status);
            }
            if(job != null){
                existingQuery.setJob(job);
            }

           return queryRepository.save(existingQuery);

        }

        return existingQuery;
    }
}
