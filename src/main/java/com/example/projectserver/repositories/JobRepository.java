package com.example.projectserver.repositories;

import com.example.projectserver.models.Company;
import com.example.projectserver.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface JobRepository extends JpaRepository<Job, Integer> {

    @Query("select  j from Job j where j.apiId=:apiId")
    Optional<Job> findJobByApiId (@Param("apiId") String apiId);

    @Query("select  j from Job j where j.keywords LIKE %:keyword%")
    Set<Job> findJobsByKeyword (@Param("keyword") String keyword);

    @Query("select  j from Job j where j.company=:company")
    List<Job> findJobsByCompany (@Param("company") Company company);
}
