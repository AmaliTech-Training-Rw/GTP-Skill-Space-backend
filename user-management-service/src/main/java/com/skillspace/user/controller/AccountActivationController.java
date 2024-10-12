package com.skillspace.user.controller;

import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.ActivationCode;
import com.skillspace.user.service.AccountService;
import com.skillspace.user.service.ActivationCodeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/account")
public class AccountActivationController {

    @Autowired
    private ActivationCodeService activationCodeService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam String code) {
        try {
            ActivationCode activationCode = activationCodeService.findCode(code);

            if (activationCode.getExpirationTime().isBefore(LocalDateTime.now())) {
                return ResponseEntity.badRequest().body("Activation code has expired");
            }

            Account account = activationCode.getAccount();
            accountService.activateAccount(account.getEmail());

            // Remove the used code
            activationCodeService.deleteCode(activationCode.getCode());

            return ResponseEntity.ok("Account activated successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Invalid activation code");
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Account not found");
        }
    }
}