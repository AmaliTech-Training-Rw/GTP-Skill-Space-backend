package com.skillspace.user.repository;

import com.skillspace.user.entity.PasswordResetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetRequestRepository extends JpaRepository<PasswordResetRequest, Long> {
    Optional<PasswordResetRequest> findByEmailAndOtp(String email, String otp);
    Optional<PasswordResetRequest> findByEmailAndValidated(String email, boolean validated);
}
