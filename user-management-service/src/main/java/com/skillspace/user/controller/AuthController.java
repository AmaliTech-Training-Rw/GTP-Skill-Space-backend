package com.skillspace.user.controller;

import com.skillspace.user.dto.AuthRequest;
import com.skillspace.user.dto.AuthResponse;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.AccountStatus;
import com.skillspace.user.jwt.JwtHandler;
import com.skillspace.user.repository.AccountRepository;
import com.skillspace.user.repository.ActivationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtHandler jwtHandler;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    ActivationCodeRepository activationCodeRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            Account account = accountRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (account.getStatus() != AccountStatus.ACTIVE) {   // Check if the account status is active
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new AuthResponse("Account not active"));
            }
            String token = jwtHandler.generateToken(request.getEmail(), account.getRole());
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid credentials"));
        }
    }

}