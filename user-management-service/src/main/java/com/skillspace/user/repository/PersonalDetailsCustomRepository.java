package com.skillspace.user.repository;

import com.skillspace.user.entity.Education;
import com.skillspace.user.entity.PersonalDetails;

import java.util.Optional;

public interface PersonalDetailsCustomRepository {
    Optional<PersonalDetails> findPersonalDetailsByTalentId(Long talentId);
    Optional<Education> findEducationByTalentId(Long talentId);
}
