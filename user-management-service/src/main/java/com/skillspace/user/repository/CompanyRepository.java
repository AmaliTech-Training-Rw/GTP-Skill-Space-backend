package com.skillspace.user.repository;

import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByUserId(Account account);

    @Query("SELECT c FROM Company c WHERE c.userId.status = 'PENDING'")
    List<Company> findPendingCompanies();

    Company findByName(String name);
}


