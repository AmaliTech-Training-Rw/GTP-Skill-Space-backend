package com.skillspace.user.controller;

import com.skillspace.user.dto.EmailDTO;
import com.skillspace.user.dto.OtpValidationDTO;
import com.skillspace.user.dto.PasswordResetDTO;
import com.skillspace.user.dto.PasswordResetResponse;
import com.skillspace.user.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    // method to initiate password reset
    @PostMapping("/initiate")
    public ResponseEntity<PasswordResetResponse> initiatePasswordReset(@RequestBody EmailDTO emailDTO) {
        PasswordResetResponse response = passwordResetService.handleInitiatePasswordReset(emailDTO.getEmail());

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else if ("EMAIL_NOT_FOUND".equals(response.getErrorCode())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // method to validate OTP
    @PostMapping("/validate-otp")
    public ResponseEntity<PasswordResetResponse> validateOTP(@RequestBody OtpValidationDTO otpValidationDTO) {
        PasswordResetResponse response = passwordResetService.handleValidateOTP(otpValidationDTO.getEmail(), otpValidationDTO.getOtp());

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // method to reset the password
    @PostMapping("/reset")
    public ResponseEntity<PasswordResetResponse> resetPassword(@RequestBody PasswordResetDTO passwordResetDTO) {
        PasswordResetResponse response = passwordResetService.handleMatchResetPassword(
                passwordResetDTO.getEmail(),
                passwordResetDTO.getNewPassword(),
                passwordResetDTO.getConfirmPassword()
        );

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}