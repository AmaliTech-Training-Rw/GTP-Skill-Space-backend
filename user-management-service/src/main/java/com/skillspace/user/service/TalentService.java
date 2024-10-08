package com.skillspace.user.service;

import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Talent;
import com.skillspace.user.repository.TalentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class TalentService extends UserRegistrationService<Talent> {

    @Autowired
    private TalentRepository talentRepository;

    @Override
    protected Talent saveUser(Talent talent, Account savedAccount) {
        // Link the Talent entity to the saved Account entity
        talent.setUserId(savedAccount);
        talent.setCreatedAt(LocalDateTime.now());
        talent.setUpdatedAt(LocalDateTime.now());

        return talentRepository.save(talent);
    }
}


