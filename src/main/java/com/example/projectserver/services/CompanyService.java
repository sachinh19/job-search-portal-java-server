package com.example.projectserver.services;

import com.example.projectserver.models.Company;
import com.example.projectserver.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CompanyService {

    private CompanyRepository companyRepository;

    @Autowired
    CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping("api/company")
    public List<Company> findAllCompanies() {

        return companyRepository.findAll();
    }

    @GetMapping("api/company/{companyId}")
    public Company findCompanyById(@PathVariable("companyId") int companyId) {
        return companyRepository.findById(companyId).orElse(null);
    }

    @PostMapping("api/company")
    public Company createCompany(@RequestBody Company company) {

        return companyRepository.save(company);
    }

    @DeleteMapping("api/company/{companyId}")
    public void deleteCompanyById(@PathVariable("companyId") int companyId) {

        Company existingCompany = companyRepository.findById(companyId).orElse(null);

        if (existingCompany != null) {

            companyRepository.delete(existingCompany);
        }
    }


    @PutMapping("api/company/{companyId}")
    public Company updateCompany(@PathVariable("companyId") int companyId,
                                 @RequestBody Company newCompany,
                                 HttpServletResponse response) {

        Company existingCompany = companyRepository.findById(companyId).orElse(null);

        if (existingCompany != null) {

            String name = newCompany.getName();
            String state = newCompany.getState();
            String country = newCompany.getCountry();

            if (name != null) {
                existingCompany.setName(name);
            }
            if (country != null) {
                existingCompany.setCountry(country);
            }
            if (state != null) {
                existingCompany.setState(state);
            }

            return companyRepository.save(existingCompany);
        }

        response.setStatus(204);
        return existingCompany;
    }
}
