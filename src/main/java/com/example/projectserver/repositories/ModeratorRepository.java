package com.example.projectserver.repositories;

import com.example.projectserver.models.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModeratorRepository extends JpaRepository<Moderator,Integer> {
}
