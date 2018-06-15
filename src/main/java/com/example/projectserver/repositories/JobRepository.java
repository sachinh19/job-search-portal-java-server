package com.example.projectserver.repositories;

import com.example.projectserver.models.Company;
import com.example.projectserver.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Integer> {

    @Query("select  j from Job j where j.apiId=:apiId")
    Optional<Job> findJobByApiId (@Param("apiId") String apiId);
}
