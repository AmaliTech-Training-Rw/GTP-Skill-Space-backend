package com.skillspace.user.service;

import com.skillspace.user.dto.TalentRegistrationRequest;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.AccountStatus;
import com.skillspace.user.entity.UserRole;
import com.skillspace.user.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

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

    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Account not found with ID: " + id));
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

    public boolean accountExists(String email) {
        return accountRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public void activateAccount(String email) throws AccountNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("Account not found with email: " + email));
        account.setStatus(AccountStatus.ACTIVE);
        accountRepository.save(account);
    }

    // Helper method to create Account from TalentRegistrationRequest
    public Account createAccountFromRequest(TalentRegistrationRequest request) {
        Account account = new Account();
        account.setEmail(request.getEmail().trim());
        account.setPassword(request.getPassword().trim());
        account.setContact(request.getContact().trim());
        account.setRole(UserRole.TALENT);
        return account;
    }
}

