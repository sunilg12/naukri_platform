package com.naukri.database_api.controllers;

import main.com.naukri.database_api.models.AppUser;
import com.naukri.database_api.models.Company;
import main.com.naukri.database_api.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/db/company")
public class CompanyController {

    CompanyRepository companyrepo;

    @Autowired
    public  CompanyController(CompanyRepository companyrepo){
        this.companyrepo = companyrepo;
    }

    @PostMapping("/save")
    public ResponseEntity createCompany(@RequestBody Company company){
        companyrepo.save(company);
        return new ResponseEntity(company,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCompanyById(@PathVariable UUID id){
        Company company = companyrepo.findById(id).orElse(null);
        return new ResponseEntity(company, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCompany(@RequestBody Company company){
        companyrepo.save(company);
        return new ResponseEntity(company, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable UUID id){
        companyrepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getUserBasedOnCompanyJobId")
    public List<AppUser> getUserBasedOnCompanyAndJobId(String name, UUID jobId){
        return companyrepo.getUsersBasedOnCompanyAndJobId(name, jobId);
    }
}
