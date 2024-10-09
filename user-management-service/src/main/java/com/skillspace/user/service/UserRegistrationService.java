package com.skillspace.user.service;

import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.AccountStatus;
import com.skillspace.user.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public abstract class UserRegistrationService<T> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    // Common method to register any type of user (Admin, Company, Talent)
    public T registerUser(T user, Account account) {
        // Encrypt the password and set it in the Account entity
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        // Set the status and other shared fields in Account
        account.setStatus(AccountStatus.PENDING);
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        // Save the account first
        Account savedAccount = accountRepository.save(account);

        // Let the subclass handle the specific user linking and saving
        return saveUser(user, savedAccount);
    }

    // Subclasses must implement this to handle user-specific logic
    protected abstract T saveUser(T user, Account savedAccount);
}

