package com.example.projectserver.repositories;

import com.example.projectserver.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin,Integer> {
    @Query("SELECT a FROM Admin a WHERE a.username=:username")
    Optional<Admin> findAdminByUsername(@Param("username") String username);

}
