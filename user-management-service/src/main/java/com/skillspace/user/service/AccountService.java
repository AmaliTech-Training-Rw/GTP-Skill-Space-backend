package com.skillspace.user.service;

import com.skillspace.user.dto.CompanyRegistrationRequest;
import com.skillspace.user.dto.TalentRegistrationRequest;
import com.skillspace.user.entity.*;
import com.skillspace.user.repository.AccountRepository;
import com.skillspace.user.repository.CompanyRepository;
import com.skillspace.user.repository.TalentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.security.auth.login.AccountNotFoundException;
import java.util.Collections;
import java.util.List;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TalentRepository talentRepository;

    // check if account  exists
    public boolean accountExists(String email) {
        return accountRepository.findByEmail(email).isPresent();
    }

    // method to verify talent account setting its status to ACTIVE
    @Transactional
    public void activateAccount(String email) throws AccountNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with email: " + email));
        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);
    }

    // method to verify company account changing its status to PENDING_APPROVAL
    @Transactional
    public void verifyAccount(String email) throws AccountNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with email: " + email));
        account.setStatus(AccountStatus.PENDING_APPROVAL);
        accountRepository.save(account);
    }

    // Helper method to create Account from TalentRegistrationRequest
    public Account createTalentAccountFromRequest(TalentRegistrationRequest request) {
        Account account = new Account();
        account.setEmail(request.getEmail().trim());
        account.setPassword(request.getPassword().trim());
        account.setContact(request.getContact().trim());
        account.setRole(UserRole.TALENT);
        return account;
    }

    // Helper method to create Account from CompanyRegistrationRequest
    public Account createCompanyAccountFromRequest(CompanyRegistrationRequest request) {
        Account account = new Account();
        account.setEmail(request.getEmail().trim());
        account.setPassword(request.getPassword().trim());
        account.setContact(request.getContact().trim());
        account.setRole(UserRole.COMPANY);
        return account;
    }

    // method to activate talent account or verify company account
    @Transactional
    public void processAccountActivation(Account account) throws AccountNotFoundException {
        if (account.getRole() == UserRole.TALENT) {
            activateAccount(account.getEmail());
            ResponseEntity.ok("Talent account activated successfully");
        } else if (account.getRole() == UserRole.COMPANY) {
            verifyAccount(account.getEmail());
            ResponseEntity.ok("Company account verified successfully. An admin will review your application.");
        } else {
            ResponseEntity.badRequest().body("Invalid account type");
        }
    }
    // method to trigger kafka for activation code sending event
    public void triggerKafkaVerificationEvent(Account account, String email, String activationCode) {
        if (account.getRole() == UserRole.COMPANY) {
            Company company = companyRepository.findByUserId(account)
                    .orElseThrow(() -> new IllegalArgumentException("Company not found for account: " + account.getId()));
            kafkaProducerService.sendCompanyVerificationCodeEvent(company, email, activationCode);
        } else if (account.getRole() == UserRole.TALENT) {
            Talent talent = talentRepository.findByUserId(account)
                    .orElseThrow(() -> new IllegalArgumentException("Talent not found for account: " + account.getId()));
            kafkaProducerService.sendTalentActivationCodeEvent(talent, email, activationCode);
        } else {
            throw new IllegalArgumentException("Unsupported account role: " + account.getRole());
        }
    }

    // find/get account in database by email
    public Account findByEmail (String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with email: " + email));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Get the role from the account entity
        UserRole userRole = account.getRole();

        // Create a list of GrantedAuthority based on the UserRole enum
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userRole.name()));

        return new org.springframework.security.core.userdetails.User(account.getEmail(), account.getPassword(), authorities);
    }
}

