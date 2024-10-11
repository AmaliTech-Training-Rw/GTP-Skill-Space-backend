package com.skillspace.user.controller;

import com.skillspace.user.dto.CompanyRegistrationRequest;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Company;
import com.skillspace.user.entity.UserRole;
import com.skillspace.user.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Company> registerCompany(@RequestBody CompanyRegistrationRequest request) {
        Account account = createAccountFromRequest(request);
        Company company = createCompanyFromRequest(request);

        Company registeredCompany = companyService.registerUser(company, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredCompany);
    }

    // Helper method to create Account from CompanyRegistrationRequest
    private Account createAccountFromRequest(CompanyRegistrationRequest request) {
        Account account = new Account();
        account.setEmail(request.getEmail().trim());
        account.setPassword(request.getPassword().trim());
        account.setContact(request.getContact().trim());
        account.setRole(UserRole.COMPANY);
        return account;
    }

    // Helper method to create Company from CompanyRegistrationRequest
    private Company createCompanyFromRequest(CompanyRegistrationRequest request) {
        Company company = new Company();
        company.setName(request.getName().trim());
        company.setCertificate(request.getCertificate().trim());
        company.setLogo(request.getLogo().trim());
        company.setWebsite(request.getWebsite().trim());
        return company;
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


