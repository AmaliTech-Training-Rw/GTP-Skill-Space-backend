package com.skillspace.user.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.skillspace.user.dto.OAuthResponse;
import com.skillspace.user.entity.Account;
import com.skillspace.user.exception.TokenVerificationException;
import com.skillspace.user.jwt.JwtHandler;
import com.skillspace.user.repository.AccountRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@Slf4j
public class OAuthService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtHandler jwtHandler;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    private GoogleIdTokenVerifier verifier;

    @PostConstruct
    public void init() {
        HttpTransport transport = new NetHttpTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        this.verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }

    public OAuthResponse authenticateWithGoogle(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdToken.Payload payload = verifyAndExtractPayload(idTokenString);
        Account account = findOrUpdateAccount(payload);
        return createAuthenticationResponse(account);
    }

    private GoogleIdToken.Payload verifyAndExtractPayload(String idTokenString) throws GeneralSecurityException, IOException {
        log.debug("Attempting to verify token: {}", idTokenString);
        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken == null) {
            log.error("Token verification failed");
            throw new TokenVerificationException("Invalid ID token");
        }

        return idToken.getPayload();
    }

    private Account findOrUpdateAccount(GoogleIdToken.Payload payload) throws AccountNotFoundException {
        String email = payload.getEmail();
        String googleId = payload.getSubject();
        log.debug("Token verified for email: {}", email);

        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("No account found with email: " + email));

        updateGoogleLinkIfNeeded(account, googleId);
        return account;
    }

    private void updateGoogleLinkIfNeeded(Account account, String googleId) {
        if (!account.isGoogleLinked()) {
            account.setGoogleId(googleId);
            account.setGoogleLinked(true);
            accountRepository.save(account);
        }
    }

    private OAuthResponse createAuthenticationResponse(Account account) {
        String jwt = jwtHandler.generateToken(account.getEmail(), account.getRole());
        return new OAuthResponse(jwt, account.getEmail(), account.getRole());
    }
}
