package com.example.projectserver.repositories;

import com.example.projectserver.models.Moderator;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ModeratorRepository extends JpaRepository<Moderator,Integer> {
    @Query("SELECT a FROM Moderator a WHERE a.username=:username")
    Optional<Moderator> findModeratorByUsername(@Param("username") String username);
}
