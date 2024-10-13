package com.skillspace.user.service;

import com.skillspace.user.dto.PasswordResetResponse;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.PasswordResetRequest;
import com.skillspace.user.repository.AccountRepository;
import com.skillspace.user.repository.PasswordResetRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class PasswordResetService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordResetRequestRepository passwordResetRequestRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final int OTP_EXPIRATION_MINUTES = 10;
    private static final int OTP_LENGTH = 5;

    // method to handle initiate password reset logic and trigger kafka send OTP
    public PasswordResetResponse handleInitiatePasswordReset(String email) {
        try{
            Optional<Account> accountOptional = accountRepository.findByEmail(email);
            if (accountOptional.isEmpty()) {
                return new PasswordResetResponse(false, "No account found with this email address", "EMAIL_NOT_FOUND");
            }
            Account account = accountOptional.get();
        String otp = generateOTP();
        PasswordResetRequest request = new PasswordResetRequest();
        request.setEmail(email);
        request.setOtp(otp);
        passwordResetRequestRepository.save(request);

        kafkaProducerService.sendPasswordResetOTPEvent(account.getEmail(), otp);
            return new PasswordResetResponse(true, "Password reset initiated. Check your email for the OTP.", null);
        } catch (Exception e) {
            return new PasswordResetResponse(false, "An error occurred while initiating password reset", "INTERNAL_ERROR");
        }
    }

    // method to handle validating OTP code
    public PasswordResetResponse handleValidateOTP(String email, String otp) {
        Optional<PasswordResetRequest> requestOpt = passwordResetRequestRepository.findByEmailAndOtp(email, otp);
        if (requestOpt.isPresent()) {
            PasswordResetRequest request = requestOpt.get();
            if (isOtpExpired(request)) {
                return new PasswordResetResponse(false, "OTP has expired", "OTP_EXPIRED");
            }
            request.setValidated(true);
            passwordResetRequestRepository.save(request);
            return new PasswordResetResponse(true, "OTP validated successfully", null);
        }
        return new PasswordResetResponse(false, "Invalid OTP", "INVALID_OTP");
    }

    // method to handle password reset
    public PasswordResetResponse handleMatchResetPassword(String email, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            return new PasswordResetResponse(false, "Passwords do not match", "PASSWORD_MISMATCH");
        }

        Optional<PasswordResetRequest> requestOpt = passwordResetRequestRepository.findByEmailAndValidated(email, true);
        if (requestOpt.isPresent()) {
            Optional<Account> accountOpt = accountRepository.findByEmail(email);
            if (accountOpt.isPresent()) {
                Account account = accountOpt.get();
                account.setPassword(encryptPassword(newPassword));
                accountRepository.save(account);
                passwordResetRequestRepository.delete(requestOpt.get());
                kafkaProducerService.sendPasswordChangedEvent(account);
                return new PasswordResetResponse(true, "Password reset successfully", null);
            }
        }
        return new PasswordResetResponse(false, "Invalid reset request", "INVALID_REQUEST");
    }

    private boolean isOtpExpired(PasswordResetRequest request) {
        LocalDateTime now = LocalDateTime.now();
        long minutesPassed = ChronoUnit.MINUTES.between(request.getCreatedAt(), now);
        return minutesPassed > OTP_EXPIRATION_MINUTES;
    }

    private String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generateOTP() {
        StringBuilder otp = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }
}


