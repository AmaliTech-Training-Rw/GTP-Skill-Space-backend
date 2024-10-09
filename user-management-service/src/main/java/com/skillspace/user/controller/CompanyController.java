package com.skillspace.user.controller;

import com.skillspace.user.dto.CompanyRegistrationRequest;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Company;
import com.skillspace.user.entity.UserRole;
import com.skillspace.user.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/register")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

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
        account.setEmail(request.getEmail());
        account.setPassword(request.getPassword());
        account.setContact(request.getContact());
        account.setRole(UserRole.COMPANY);
        return account;
    }

    // Helper method to create Company from CompanyRegistrationRequest
    private Company createCompanyFromRequest(CompanyRegistrationRequest request) {
        Company company = new Company();
        company.setName(request.getName());
        company.setCertificate(request.getCertificate());
        company.setLogo(request.getLogo());
        company.setWebsite(request.getWebsite());
        return company;
    }
}


