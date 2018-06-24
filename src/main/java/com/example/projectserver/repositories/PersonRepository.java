package com.example.projectserver.repositories;

import com.example.projectserver.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("SELECT u FROM Person u WHERE u.username=:username AND u.password=:password")
    Optional<Person> findPersonByCredentials(@Param("username") String username, @Param("password") String password);

    @Query("SELECT u FROM Person u WHERE u.username=:username")
    Optional<Person> findPersonByUsername(@Param("username") String username);

}
