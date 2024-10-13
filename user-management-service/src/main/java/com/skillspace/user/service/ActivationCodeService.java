package com.skillspace.user.service;

import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.ActivationCode;
import com.skillspace.user.repository.AccountRepository;
import com.skillspace.user.repository.ActivationCodeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
public class ActivationCodeService {
    @Autowired
    private ActivationCodeRepository activationCodeRepository;

    @Autowired
    private AccountRepository accountRepository;

    // method to create an activation code
    public String createActivationCode(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        ActivationCode code = new ActivationCode();
        code.setAccount(account);
        code.setCode(generateCode());
        activationCodeRepository.save(code);
        return code.getCode();
    }

    //helper method to generate OTP code
    private String generateCode() {
        return String.format("%05d", new Random().nextInt(100000));
    }

    // method to find OTP code in db
    @Transactional
    public ActivationCode findCode(String code) {
        return activationCodeRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Activation Code not found"));
    }

    // method to delete OTP code
    @Transactional
    public void deleteCode(String code) {
        activationCodeRepository.deleteByCode(code);
    }

    //method to find account by account ID
    public Optional<ActivationCode> findByAccountId(Long accountId) {
        return activationCodeRepository.findById(accountId);
    }
}