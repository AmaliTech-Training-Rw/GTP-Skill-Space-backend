package com.skillspace.user.controller;

import com.skillspace.user.dto.CompanyRegistrationRequest;
import com.skillspace.user.entity.Company;
import com.skillspace.user.exception.AccountAlreadyExistsException;
import com.skillspace.user.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/register")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/company")
    public ResponseEntity<?> registerCompany(@Valid @RequestBody CompanyRegistrationRequest request) {
        Company registeredCompany = companyService.registerCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredCompany);
    }

    @GetMapping("/all")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{name}")
    public Company getCompanyByName(@PathVariable String name) {
        return companyService.findByName(name);
    }
}


