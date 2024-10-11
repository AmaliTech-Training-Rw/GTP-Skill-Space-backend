package com.skillspace.user.repository;

import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByUserId(Account account);
    Company findByName(String name);
}


