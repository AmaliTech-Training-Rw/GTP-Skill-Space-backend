package com.skillspace.user.service;

import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Company;
import com.skillspace.user.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService extends UserRegistrationService<Company> {

    @Autowired
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    protected Company saveUser(Company company, Account savedAccount) {
        // Link the Company entity to the saved Account entity
        company.setUserId(savedAccount);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }
    public Company findByName(String name) {
        return companyRepository.findByName(name);
    }
    public Optional<Company> findCompanyById(long companyId) {
        return companyRepository.findById(companyId);
    }

}
