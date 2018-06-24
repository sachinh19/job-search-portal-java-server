package com.example.projectserver.repositories;

import com.example.projectserver.models.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployerRepository extends JpaRepository<Employer, Integer> {
    @Query("SELECT a FROM Employer a WHERE a.username=:username")
    Optional<Employer> findEmployerByUsername(@Param("username") String username);
}
