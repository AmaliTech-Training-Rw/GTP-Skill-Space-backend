package com.skillspace.user.service;

import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.ActivationToken;
import com.skillspace.user.repository.AccountRepository;
import com.skillspace.user.repository.ActivationTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class ActivationTokenService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ActivationTokenRepository activationTokenRepository;

    public void createActivationToken(Long accountId) {
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if (accountOptional.isPresent()) {
            // Create the Activation Token and assign the Account
            ActivationToken token = new ActivationToken();
            token.setAccount(accountOptional.get());
            token.setToken(generateToken()); //
            activationTokenRepository.save(token);
        } else {
            throw new EntityNotFoundException("Account not found");
        }
    }

    // Add token generation logic
    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public ActivationToken findToken(String token) {
        Optional<ActivationToken> tokenOptional = activationTokenRepository.findByToken(token);

        if (tokenOptional.isPresent()) {
            return tokenOptional.get();
        } else {
            throw new EntityNotFoundException("Activation Token not found");
        }
    }

}

