package com.skillspace.user.service;

import com.skillspace.user.dto.FileUploadResponse;
import com.skillspace.user.dto.UpdateCompany;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.AccountStatus;
import com.skillspace.user.entity.Company;
import com.skillspace.user.repository.AccountRepository;
import com.skillspace.user.repository.CompanyRepository;
import com.skillspace.user.util.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

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
    public CustomResponse<Company> updateCompany(Long companyId, UpdateCompany updatedCompany, MultipartFile reqCertificate, MultipartFile reqLogo) {

        try {
            Optional<Company> existingCompanyOpt = companyRepository.findById(companyId);

            if (existingCompanyOpt.isPresent()) {
                Company existingCompany = existingCompanyOpt.get();
                updateCompanyFields(existingCompany, updatedCompany, reqCertificate, reqLogo);

                return new CustomResponse<>("Company updated successfully", HttpStatus.OK.value(), companyRepository.save(existingCompany));
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
            }
        } catch (Exception e) {
            return new CustomResponse<>("Failed to update company: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    //Helper
    private void updateCompanyFields(Company existingCompany, UpdateCompany updatedCompany, MultipartFile reqCertificate, MultipartFile reqLogo) {
        Optional.ofNullable(updatedCompany.getName())
                .filter(name -> !name.isEmpty())
                .ifPresent(existingCompany::setName);

        Optional.ofNullable(updatedCompany.getContact())
                .filter(contact -> !contact.isEmpty())
                .ifPresent(contact -> {
                    Long userId = existingCompany.getUserId().getId();
                    Optional<Account> companyAccountExisting = accountRepository.findById(userId);
                    companyAccountExisting.ifPresent(account -> {
                        account.setContact(contact);
                        accountRepository.save(account);
                    });
                });

//        Optional.ofNullable(updatedCompany.getCertificate())
        Optional.ofNullable(reqCertificate)
                .filter(certificateFile -> !certificateFile.isEmpty())
                .ifPresent(certificateFile -> {
                    FileUploadResponse certificateUploadResponse = fileUploadService.uploadFile(certificateFile);
                    existingCompany.setCertificate(certificateUploadResponse.getFilePath());
                });

//        Optional.ofNullable(updatedCompany.getLogo())
        Optional.ofNullable(reqLogo)
                .filter(logoFile -> !logoFile.isEmpty())
                .ifPresent(logoFile -> {
                    FileUploadResponse logoUploadResponse = fileUploadService.uploadFile(logoFile);
                    existingCompany.setLogo(logoUploadResponse.getFilePath());
                });

        Optional.ofNullable(updatedCompany.getWebsite())
                .filter(website -> !website.isEmpty())
                .ifPresent(existingCompany::setWebsite);
    }
}
