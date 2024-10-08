package com.skillspace.user.repository;

import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Talent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TalentRepository extends JpaRepository<Talent, Long> {
    Optional<Talent> findByUserId(Account account);
}


