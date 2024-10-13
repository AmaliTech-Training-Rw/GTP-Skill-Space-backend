package com.skillspace.user.service;

import com.skillspace.user.dto.CompanyRegistrationRequest;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.AccountStatus;
import com.skillspace.user.entity.Company;
import com.skillspace.user.entity.UserRole;
import com.skillspace.user.exception.AccountAlreadyExistsException;
import com.skillspace.user.repository.AccountRepository;
import com.skillspace.user.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompanyService extends UserRegistrationService<Company> {

    @Autowired
    private final CompanyRepository companyRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ActivationCodeService activationCodeService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    // method to handle register company logic and trigger kafka send verification code
    @Transactional
    public Company registerCompany(CompanyRegistrationRequest request) {
        Account account = accountService.createCompanyAccountFromRequest(request);

        // Check if the account already exists
        if (accountService.accountExists(account.getEmail())) {
            throw new AccountAlreadyExistsException("Account with associated email: " + account.getEmail() + " already exists");
        }

        Company company = createCompanyFromRequest(request);
        Company registeredCompany = this.registerUser(company, account);

        // Create and send activation code event after successful registration
        String activationCode = activationCodeService.createActivationCode(account.getId());
        kafkaProducerService.sendCompanyVerificationCodeEvent(registeredCompany, account.getEmail(), activationCode);

        return registeredCompany;
    }

    // helper method to save a company user
    @Override
    protected Company saveUser(Company company, Account savedAccount) {
        // Link the Company entity to the saved Account entity
        company.setUserId(savedAccount);
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());

        return companyRepository.save(company);
    }

    // method to fetch companies with PENDING status
    public List<Company> getPendingCompanies() {
        return companyRepository.findPendingCompanies();
    }

    // method to approve company by setting the associated account's status to ACTIVE
    @Transactional
    public Company approveCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        Account account = company.getUserId();
        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);
        return company;
    }

    // method to reject company by setting the associated account's status to REJECTED
    @Transactional
    public Company rejectCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        Account account = company.getUserId();
        account.setStatus(AccountStatus.REJECTED);
        accountRepository.save(account);
        return company;
    }

    // Helper method to create Company from CompanyRegistrationRequest
    public Company createCompanyFromRequest(CompanyRegistrationRequest request) {
        Company company = new Company();
        company.setName(request.getName().trim());
        company.setCertificate(request.getCertificate().trim());
        company.setLogo(request.getLogo().trim());
        company.setWebsite(request.getWebsite().trim());
        return company;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company findByName(String name) {
        return companyRepository.findByName(name);
    }

}
