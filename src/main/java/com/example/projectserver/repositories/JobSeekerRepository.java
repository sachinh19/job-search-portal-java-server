package com.example.projectserver.repositories;

import com.example.projectserver.models.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, Integer> {
    @Query("SELECT a FROM JobSeeker a WHERE a.username=:username")
    Optional<JobSeeker> findJobSeekerByUsername(@Param("username") String username);
}
