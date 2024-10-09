package com.skillspace.assessment.repositories;

import com.skillspace.assessment.model.Quiz;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuizRepository extends CassandraRepository<Quiz, UUID> {
}


