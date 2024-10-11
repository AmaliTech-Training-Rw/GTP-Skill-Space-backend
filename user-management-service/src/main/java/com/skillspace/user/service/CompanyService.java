package com.skillspace.user.service;

import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.AccountStatus;
import com.skillspace.user.entity.Company;
import com.skillspace.user.repository.AccountRepository;
import com.skillspace.user.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompanyService extends UserRegistrationService<Company> {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    protected Company saveUser(Company company, Account savedAccount) {
        // Link the Company entity to the saved Account entity
        company.setUserId(savedAccount);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        return companyRepository.save(company);
    }

    // Fetch companies with PENDING status
    public List<Company> getPendingCompanies() {
        return companyRepository.findPendingCompanies();
    }

    // method to approve company by setting the associated account's status to ACTIVE
    public void approveCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        Account account = company.getUserId();
        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);
    }

    // method to reject company by setting the associated account's status to REJECTED
    public void rejectCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        Account account = company.getUserId();
        account.setStatus(AccountStatus.REJECTED);
        accountRepository.save(account);
    }
}
