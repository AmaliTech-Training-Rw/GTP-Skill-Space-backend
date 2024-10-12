package com.skillspace.user.repository;

import com.skillspace.user.entity.ActivationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivationCodeRepository extends JpaRepository<ActivationCode, Long> {

    Optional<ActivationCode> findByCode(String code);

    void deleteByCode(String code);
}


