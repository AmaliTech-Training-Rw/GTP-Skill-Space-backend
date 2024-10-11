package com.skillspace.user.controller;

import com.skillspace.user.dto.TalentRegistrationRequest;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Talent;
import com.skillspace.user.entity.UserRole;
import com.skillspace.user.service.TalentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth/register")
@Validated
public class TalentController {

    @Autowired
    private TalentService talentService;

    @PostMapping("/talent")
    public ResponseEntity<Talent> registerTalent(@Valid @RequestBody TalentRegistrationRequest request) {
        Account account = createAccountFromRequest(request);
        Talent talent = createTalentFromRequest(request);

        Talent registeredTalent = talentService.registerUser(talent, account);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredTalent);
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
    @GetMapping("/talent/{id}")
    public ResponseEntity<?> getTalentById(@PathVariable Long id) {
        Optional<Talent> talent = talentService.getTalentById(id);
        if (talent.isPresent()) {
            return ResponseEntity.ok(talent.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}





