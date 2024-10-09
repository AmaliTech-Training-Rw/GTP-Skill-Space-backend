package com.skillspace.user.repository;

import com.skillspace.user.entity.Education;
import com.skillspace.user.entity.PersonalDetails;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EducationRepository extends CassandraRepository<Education, UUID> {
}

