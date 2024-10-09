package com.skillspace.user.service;

import com.skillspace.user.dto.FileUploadResponse;
import com.skillspace.user.dto.UpdateCompany;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.AccountStatus;
import com.skillspace.user.entity.Company;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CompanyService extends UserRegistrationService<Company> {

    @Autowired
    private final CompanyRepository companyRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private  FileUploadService fileUploadService;

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

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company findByName(String name) {
        return companyRepository.findByName(name);
    }

    @Transactional
    public Company updateCompany(Long companyId, UpdateCompany updatedCompany) {
        Optional<Company> existingCompanyOpt = companyRepository.findById(companyId);

        if (existingCompanyOpt.isPresent()) {
            Company existingCompany = existingCompanyOpt.get();
            Optional<Account> companyAccountExisting = accountRepository.findById(existingCompany.getUserId().getId());

            if (updatedCompany.getName() != null && !updatedCompany.getName().isEmpty()) {
                existingCompany.setName(updatedCompany.getName());
            }

            if (updatedCompany.getContact() != null && !updatedCompany.getContact().isEmpty() && companyAccountExisting.isPresent()) {
                Account companyAccount = companyAccountExisting.get();
                companyAccount.setContact(updatedCompany.getContact());
                accountRepository.save(companyAccount);
            }

            if(updatedCompany.getCertificate() != null && !updatedCompany.getCertificate().isEmpty()) {
                MultipartFile certificateFile = updatedCompany.getCertificate();
                FileUploadResponse certificateUploadResponse = fileUploadService.uploadFile(certificateFile);
                existingCompany.setCertificate(certificateUploadResponse.getFilePath());
            }

            if(updatedCompany.getLogo() != null && !updatedCompany.getLogo().isEmpty()) {
                MultipartFile logoFile = updatedCompany.getLogo();
                FileUploadResponse logoUploadResponse = fileUploadService.uploadFile(logoFile);
                existingCompany.setCertificate(logoUploadResponse.getFilePath());
            }

            if (updatedCompany.getWebsite() != null && !updatedCompany.getWebsite().isEmpty()) {
                existingCompany.setWebsite(updatedCompany.getWebsite());
            }

            return companyRepository.save(existingCompany);
        } else {
            throw new EntityNotFoundException("Company not found");
        }
    }
}
