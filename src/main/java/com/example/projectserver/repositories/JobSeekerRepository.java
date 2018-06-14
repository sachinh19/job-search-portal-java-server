package com.example.projectserver.repositories;

import com.example.projectserver.models.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, Integer> {
}
