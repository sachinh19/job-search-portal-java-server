package com.example.projectserver.repositories;

import com.example.projectserver.models.Award;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwardRepository extends JpaRepository<Award, Integer> {
}
