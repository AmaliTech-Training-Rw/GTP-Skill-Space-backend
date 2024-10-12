package com.skillspace.assessment.repositories;

import com.skillspace.assessment.model.Quiz;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuizRepository extends CassandraRepository<Quiz, UUID> {

    @Query("SELECT * FROM quiz WHERE companyId = ?0 ALLOW FILTERING")
    List<Quiz> findByCompanyId(String companyId);

    @Query("SELECT * FROM quiz WHERE isGlobal = true ALLOW FILTERING")
    List<Quiz> findByIsGlobalTrue();

    List<Quiz> findByTitle(String title);

    @Query("SELECT * FROM quiz WHERE companyId = ?0 AND isGlobal = false ALLOW FILTERING")
    List<Quiz> findByCompanyIdAndIsGlobalFalse(String companyId);

    @Query("SELECT * FROM quiz WHERE companyId = ?0 AND title LIKE ?1 ALLOW FILTERING")
    List<Quiz> findByCompanyIdAndTitleContaining(String companyId, String title);
}