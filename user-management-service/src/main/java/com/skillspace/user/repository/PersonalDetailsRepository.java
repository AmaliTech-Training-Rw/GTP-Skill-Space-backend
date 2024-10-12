package com.skillspace.user.repository;

import com.skillspace.user.entity.PersonalDetails;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonalDetailsRepository extends CassandraRepository<PersonalDetails, UUID> {
    @Query("SELECT * FROM personaldetails WHERE talent_id = ?0 ALLOW FILTERING")
    Optional<PersonalDetails> findPersonalDetailsByTalentId(Long talentId);

}
