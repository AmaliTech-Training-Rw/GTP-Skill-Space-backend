package com.skillspace.user.controller;

import com.skillspace.user.dto.TalentAccountCreatedEvent;
import com.skillspace.user.dto.TalentRegistrationRequest;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Talent;
import com.skillspace.user.entity.UserRole;
import com.skillspace.user.exception.AccountAlreadyExistsException;
import com.skillspace.user.service.AccountService;
import com.skillspace.user.service.ActivationCodeService;
import com.skillspace.user.service.KafkaProducerService;
import com.skillspace.user.service.TalentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/register")
@Validated
public class TalentController {

    @Autowired
    private TalentService talentService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ActivationCodeService activationCodeService;

    @Autowired
    KafkaProducerService kafkaProducerService;

    @PostMapping("/talent")
    public ResponseEntity<?> registerTalent(@Valid @RequestBody TalentRegistrationRequest request) {
        Account account = accountService.createAccountFromRequest(request);
        Talent talent = talentService.createTalentFromRequest(request);

        // Check if the account already exists
        if (accountService.accountExists(account.getEmail())) {
            throw new AccountAlreadyExistsException("Account with associated email: " + account.getEmail() +  " already exists");
        }
        try {
            Talent registeredTalent = talentService.registerUser(talent, account);
            String activationCode = activationCodeService.createActivationCode(account.getId());
            kafkaProducerService.sendTalentActivationCodeEvent(registeredTalent, account.getEmail(), activationCode);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredTalent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to register talent at the moment");
        }
    }


}


