package com.skillspace.user.controller;

import com.skillspace.user.dto.ActivationCodeRequest;
import com.skillspace.user.dto.VerificationCodeDTO;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.AccountStatus;
import com.skillspace.user.entity.ActivationCode;
import com.skillspace.user.service.AccountService;
import com.skillspace.user.service.ActivationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/account")
public class NewAccountVerificationController {

    @Autowired
    private ActivationCodeService activationCodeService;

    @Autowired
    private AccountService accountService;

    // method to verify newly created account
    @PostMapping("/verify")
    public ResponseEntity<String> activateAccount(@RequestBody VerificationCodeDTO request) throws AccountNotFoundException {
        String code = request.getCode();
        ActivationCode activationCode = activationCodeService.findCode(code);

        if (isCodeExpired(activationCode)) {
            return ResponseEntity.badRequest().body("Activation code has expired");
        }

        Account account = activationCode.getAccount();

        if (!isAccountEligibleForActivation(account)) {
            return ResponseEntity.badRequest().body("Action not allowed for this account");
        }

        activateAccount(account, activationCode);
        return ResponseEntity.ok("Account activated successfully");
    }

    // method to request new verification codee
    @PostMapping("/request-new-code")
    public ResponseEntity<String> requestNewActivationCode(@RequestBody ActivationCodeRequest request) {
        String email = request.getEmail();
        Account account = accountService.findByEmail(email);

        // Check if an existing activation code exists
        Optional<ActivationCode> existingCode = activationCodeService.findByAccountId(account.getId());
        if (existingCode.isPresent()) {
            activationCodeService.deleteCode(existingCode.get().getCode());
        }

        String newActivationCode = activationCodeService.createActivationCode(account.getId());

        accountService.triggerKafkaVerificationEvent(account, email, newActivationCode);

        return ResponseEntity.ok("New activation code sent.");
    }

    // method to check if activation code is expired
    private boolean isCodeExpired(ActivationCode activationCode) {
        return activationCode.getExpirationTime().isBefore(LocalDateTime.now());
    }

    // method to check eligibility for activation by status of PENDING
    private boolean isAccountEligibleForActivation(Account account) {
        return account.getStatus() == AccountStatus.PENDING;
    }

    //method to account activation/verification
    private void activateAccount(Account account, ActivationCode activationCode) throws AccountNotFoundException {
        accountService.processAccountActivation(account);
        activationCodeService.deleteCode(activationCode.getCode());
    }

}