package com.skillspace.user.service;

import com.skillspace.user.dto.TalentRegistrationRequest;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Education;
import com.skillspace.user.entity.PersonalDetails;
import com.skillspace.user.entity.Talent;
import com.skillspace.user.repository.EducationRepository;
import com.skillspace.user.repository.PersonalDetailsRepository;
import com.skillspace.user.repository.TalentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class TalentService extends UserRegistrationService<Talent> {

    @Autowired
    private TalentRepository talentRepository;

    @Autowired
    private PersonalDetailsRepository personalDetailsRepository;

    @Autowired
    private EducationRepository educationRepository;

    @Override
    protected Talent saveUser(Talent talent, Account savedAccount) {
        PersonalDetails personalDetails = new PersonalDetails();

        // Link the Talent entity to the saved Account entity
        talent.setUserId(savedAccount);
        talent.setCreatedAt(LocalDateTime.now());
        talent.setUpdatedAt(LocalDateTime.now());

        Talent savedTalent = talentRepository.save(talent);

        personalDetails.setId(UUID.randomUUID());
        personalDetails.setTalentId(savedTalent.getTalentId());
        personalDetailsRepository.save(personalDetails);

        return savedTalent;
    }

    // Helper method to create Talent from TalentRegistrationRequest
    public Talent createTalentFromRequest(TalentRegistrationRequest request) {
        Talent talent = new Talent();
        talent.setFirstName(request.getFirstname().trim());
        talent.setLastName(request.getLastname().trim());
        return talent;
    }

}


