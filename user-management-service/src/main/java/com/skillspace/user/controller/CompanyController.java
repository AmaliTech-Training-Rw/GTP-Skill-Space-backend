package com.skillspace.user.controller;

import com.skillspace.user.dto.CompanyAccountCreatedEvent;
import com.skillspace.user.dto.CompanyRegistrationRequest;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Company;
import com.skillspace.user.entity.UserRole;
import com.skillspace.user.exception.AccountAlreadyExistsException;
import com.skillspace.user.service.AccountService;
import com.skillspace.user.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
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

    @Autowired
    private AccountService accountService;

    @PostMapping("/company")
    public ResponseEntity<?> registerCompany(@RequestBody CompanyRegistrationRequest request) {
        Account account = createAccountFromRequest(request);
        Company company = createCompanyFromRequest(request);

        // Check if the account already exists
        if (accountService.accountExists(account.getEmail())) {
            throw new AccountAlreadyExistsException("Account with associated email: " + account.getEmail() +  " already exists");
        }
        try {
        Company registeredCompany = companyService.registerUser(company, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredCompany);
    }  catch(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unable to register company at the moment");}
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


