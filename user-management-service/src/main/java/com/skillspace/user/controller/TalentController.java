package com.skillspace.user.controller;

import com.skillspace.user.dto.TalentAccountCreatedEvent;
import com.skillspace.user.dto.TalentRegistrationRequest;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Talent;
import com.skillspace.user.entity.UserRole;
import com.skillspace.user.exception.AccountAlreadyExistsException;
import com.skillspace.user.service.AccountService;
import com.skillspace.user.service.ActivationCodeService;
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
    private KafkaTemplate<String, TalentAccountCreatedEvent> kafkaTemplate;

    @Autowired
    private ActivationCodeService activationCodeService;

    @PostMapping("/talent")
    public ResponseEntity<?> registerTalent(@Valid @RequestBody TalentRegistrationRequest request) {
        Account account = createAccountFromRequest(request);
        Talent talent = createTalentFromRequest(request);

        // Check if the account already exists
        if (accountService.accountExists(account.getEmail())) {
            throw new AccountAlreadyExistsException("Account with associated email: " + account.getEmail() +  " already exists");
        }
        try {
            Talent registeredTalent = talentService.registerUser(talent, account);
            String activationCode = activationCodeService.createActivationCode(account.getId());
            handleSendKafkaEvent(registeredTalent, account.getEmail(), activationCode);
            return ResponseEntity.status(HttpStatus.CREATED).body(registeredTalent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unable to register talent at the moment");
        }
    }

    // helper method to handle Kafka event sending logic
    private void handleSendKafkaEvent(Talent registeredTalent, String email, String activationCode) {
        TalentAccountCreatedEvent event = new TalentAccountCreatedEvent();
        event.setFirstName(registeredTalent.getFirstName());
        event.setLastName(registeredTalent.getLastName());
        event.setEmail(email);
        event.setActivationCode(activationCode);
        kafkaTemplate.send("talent-accounts", event);
    }

    // Helper method to create Account from TalentRegistrationRequest
    private Account createAccountFromRequest(TalentRegistrationRequest request) {
        Account account = new Account();
        account.setEmail(request.getEmail().trim());
        account.setPassword(request.getPassword().trim());
        account.setContact(request.getContact().trim());
        account.setRole(UserRole.TALENT);
        return account;
    }

    // Helper method to create Talent from TalentRegistrationRequest
    private Talent createTalentFromRequest(TalentRegistrationRequest request) {
        Talent talent = new Talent();
        talent.setFirstName(request.getFirstname().trim());
        talent.setLastName(request.getLastname().trim());
        return talent;
    }
}


