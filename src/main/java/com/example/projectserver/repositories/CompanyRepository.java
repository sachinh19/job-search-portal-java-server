package com.example.projectserver.repositories;

import com.example.projectserver.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Integer> {

    @Query("select c from Company c where c.name=:name")
    Optional<Company> findCompanyByName (@Param("name") String name);

    @Query("select  c from Company c where c.apiId=:apiId")
    Optional<Company> findCompanyByApiId (@Param("apiId") String apiId);
}
