package com.skillspace.assessment.repositories;

import com.skillspace.assessment.model.QuizAttempt;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

public interface QuizAttemptRepository extends CassandraRepository<QuizAttempt, String> {
}
