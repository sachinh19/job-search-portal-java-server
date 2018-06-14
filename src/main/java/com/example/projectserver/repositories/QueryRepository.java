package com.example.projectserver.repositories;

import com.example.projectserver.models.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueryRepository extends JpaRepository<Query,Integer> {
}
