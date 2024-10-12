package com.skillspace.user.repository;

import com.skillspace.user.entity.Education;
import com.skillspace.user.entity.PersonalDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PersonalDetailsCustomRepositoryImpl implements PersonalDetailsCustomRepository {

    @Autowired
    private CassandraOperations cassandraTemplate;

    @Override
    public Optional<PersonalDetails> findPersonalDetailsByTalentId(Long talentId) {
        String query = "SELECT * FROM personaldetails WHERE talent_id = ?";
        PersonalDetails personalDetails = cassandraTemplate.selectOne(query, PersonalDetails.class);
        return Optional.ofNullable(personalDetails);
    }

    @Override
    public Optional<Education> findEducationByTalentId(Long talentId) {
        String query = "SELECT * FROM education WHERE talent_id = ?";
        Education educationInfo = cassandraTemplate.selectOne(query, Education.class);
        return Optional.ofNullable(educationInfo);
    }

}
