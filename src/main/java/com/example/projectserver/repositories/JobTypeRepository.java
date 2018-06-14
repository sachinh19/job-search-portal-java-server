package com.example.projectserver.repositories;

import com.example.projectserver.models.JobType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTypeRepository extends JpaRepository<JobType,Integer> {
}
