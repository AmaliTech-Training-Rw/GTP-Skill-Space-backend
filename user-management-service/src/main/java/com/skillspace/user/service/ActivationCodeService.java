package com.skillspace.user.service;

import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.ActivationCode;
import com.skillspace.user.repository.AccountRepository;
import com.skillspace.user.repository.ActivationCodeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class ActivationCodeService {
    @Autowired
    private ActivationCodeRepository activationCodeRepository;

    @Autowired
    private AccountRepository accountRepository;

    public String createActivationCode(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        ActivationCode code = new ActivationCode();
        code.setAccount(account);
        code.setCode(generateCode());
        activationCodeRepository.save(code);
        return code.getCode();
    }

    private String generateCode() {
        return String.format("%05d", new Random().nextInt(100000));
    }

    public ActivationCode findCode(String code) {
        return activationCodeRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Activation Code not found"));
    }

    @Transactional
    public void deleteCode(String code) {
        activationCodeRepository.deleteByCode(code);
    }
}