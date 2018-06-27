package com.example.projectserver.services;

import com.example.projectserver.models.Company;
import com.example.projectserver.models.Employer;
import com.example.projectserver.models.Job;
import com.example.projectserver.models.JobType;
import com.example.projectserver.repositories.CompanyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@CrossOrigin(origins = "https://team2070.herokuapp.com", maxAge = 3600, allowCredentials = "true")
public class CompanyService {

    private static final String API_URL ="https://authenticjobs.com/api/?api_key=fbf2b1502bc1ccf4aac2d014afb4ad28&method=aj.jobs.getcompanies&format=json";

    private CompanyRepository companyRepository;

    @Autowired
    CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping("api/company")
    public List<Company> findAllCompanies(HttpServletResponse response) {
                return companyRepository.findAll();
    }

    @GetMapping("api/company/{companyId}")
    public Company findCompanyById(@PathVariable("companyId") int companyId, HttpServletResponse response) {
                return companyRepository.findById(companyId).orElse(null);
    }

    @GetMapping("api/company/{companyId}/employees")
    public List<Employer> findEmployeesOfCompany(@PathVariable("companyId") int companyId, HttpServletResponse response) {
        Company company = companyRepository.findById(companyId).orElse(null);
                if (company != null) {
            return company.getEmployers();
        }
        response.setStatus(204);
        return null;
    }

    @GetMapping("api/company/{companyId}/jobs")
    public List<Job> findJobsOfCompany(@PathVariable("companyId") int companyId, HttpServletResponse response) {
        Company company = companyRepository.findById(companyId).orElse(null);
                if (company != null) {
            return company.getJobs();
        }
        response.setStatus(204);
        return null;
    }

    @PostMapping("api/company")
    public Company createCompany(@RequestBody Company company, HttpServletResponse response) {

        Company existingCompany = companyRepository.findCompanyByApiId(company.getApiId()).orElse(null);

        if(existingCompany != null){

            return updateCompany(company.getId(),company,response);
        }

        return companyRepository.save(company);

    }

    @DeleteMapping("api/company/{companyId}")
    public void deleteCompanyById(@PathVariable("companyId") int companyId, HttpServletResponse response) {
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
            String apiId = newCompany.getApiId();
            String url = newCompany.getUrl();

            if (name != null) {
                existingCompany.setName(name);
            }
            if (state != null) {
                existingCompany.setState(state);
            }
            if (apiId != null) {
                existingCompany.setApiId(apiId);
            }
            if (url != null) {
                existingCompany.setUrl(url);
            }
            return companyRepository.save(existingCompany);
        }

        response.setStatus(204);
        return existingCompany;
    }

    @GetMapping("api/getcompanies")
    public List<Company> getNewCompanies(HttpServletResponse response) throws IOException, ParseException {
        JsonNode jsonNode = getDatafromUrl();

        List<Company> companyList = new ArrayList<Company>();

        Iterator<JsonNode> itr = jsonNode.get("companies").get("company").elements();

        while (itr.hasNext()) {

            Company company = new Company();

            JsonNode newCompany = itr.next();

            if (newCompany.has("id")) {

                company.setApiId(newCompany.get("id").textValue());
            }

            if (newCompany.has("name")) {

                company.setName(newCompany.get("name").textValue());
            }
            if (newCompany.has("url")) {

                company.setUrl(newCompany.get("url").textValue());
            }

            if (newCompany.has("location")) {

                company.setState(newCompany.get("location").get("name").textValue());
            }

            companyList.add(createCompany(company,response));
        }
                return companyList;

    }


    public static JsonNode getDatafromUrl() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String s = (String)restTemplate.getForObject(API_URL, String.class);
        JsonNode jsonNode = new ObjectMapper().readTree(s);
        return jsonNode;
    }
}
