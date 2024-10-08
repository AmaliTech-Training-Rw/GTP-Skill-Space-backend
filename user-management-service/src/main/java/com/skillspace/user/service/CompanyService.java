package com.skillspace.user.service;

import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Company;
import com.skillspace.user.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CompanyService extends UserRegistrationService<Company> {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    protected Company saveUser(Company company, Account savedAccount) {
        // Link the Company entity to the saved Account entity
        company.setUserId(savedAccount);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        return companyRepository.save(company);
    }
}
