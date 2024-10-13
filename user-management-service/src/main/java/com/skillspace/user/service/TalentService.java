package com.skillspace.user.service;

import com.skillspace.user.dto.TalentRegistrationRequest;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Talent;
import com.skillspace.user.exception.AccountAlreadyExistsException;
import com.skillspace.user.repository.TalentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class TalentService extends UserRegistrationService<Talent> {

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ActivationCodeService activationCodeService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    // method to register talent user
    @Transactional
    public Talent registerTalent(TalentRegistrationRequest request) {
        Account account = accountService.createTalentAccountFromRequest(request);

        // Check if the account already exists
        if (accountService.accountExists(account.getEmail())) {
            throw new AccountAlreadyExistsException("Account with associated email: " + account.getEmail() + " already exists");
        }

        Talent talent = createTalentFromRequest(request);
        Talent registeredTalent = this.registerUser(talent, account);

        // Create and send activation code event after successful registration
        String activationCode = activationCodeService.createActivationCode(account.getId());
        kafkaProducerService.sendTalentActivationCodeEvent(registeredTalent, account.getEmail(), activationCode);

        return registeredTalent;
    }

    // helper method to save Talent user
    @Override
    protected Talent saveUser(Talent talent, Account savedAccount) {
        // Link the Talent entity to the saved Account entity
        talent.setUserId(savedAccount);
        talent.setCreatedAt(LocalDateTime.now());
        talent.setUpdatedAt(LocalDateTime.now());

        return talentRepository.save(talent);
    }

    // Helper method to create Talent from TalentRegistrationRequest
    public Talent createTalentFromRequest(TalentRegistrationRequest request) {
        Talent talent = new Talent();
        talent.setFirstName(request.getFirstname().trim());
        talent.setLastName(request.getLastname().trim());
        return talent;
    }

}


