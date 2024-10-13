package com.skillspace.user.controller;

import com.skillspace.user.dto.AuthRequest;
import com.skillspace.user.dto.AuthResponse;
import com.skillspace.user.entity.Account;
import com.skillspace.user.entity.AccountStatus;
import com.skillspace.user.entity.ActivationCode;
import com.skillspace.user.jwt.JwtHandler;
import com.skillspace.user.repository.AccountRepository;
import com.skillspace.user.repository.ActivationCodeRepository;
import com.skillspace.user.service.AccountService;
import com.skillspace.user.service.ActivationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtHandler jwtHandler;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

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

    @GetMapping("/login/oauth2/code/google")
    public ResponseEntity<AuthResponse> googleLogin(@RequestParam("code") String code) {
        try {
            ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");

            OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
                    .clientId(clientRegistration.getClientId())
                    .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
                    .redirectUri(clientRegistration.getRedirectUri())
                    .scopes(clientRegistration.getScopes())
                    .state("state")
                    .build();

            OAuth2AuthorizationResponse authorizationResponse = OAuth2AuthorizationResponse
                    .success(code)
                    .redirectUri(clientRegistration.getRedirectUri())
                    .state("state")
                    .build();

            OAuth2AuthorizationExchange authorizationExchange = new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse);
            OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> tokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
            OAuth2AuthorizationCodeGrantRequest grantRequest = new OAuth2AuthorizationCodeGrantRequest(clientRegistration, authorizationExchange);
            OAuth2AccessTokenResponse tokenResponse = tokenResponseClient.getTokenResponse(grantRequest);
            OAuth2AccessToken accessToken = tokenResponse.getAccessToken();

            // Use the access token to get user info
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken.getTokenValue());
            HttpEntity<String> entity = new HttpEntity<>("", headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri(),
                    HttpMethod.GET,
                    entity,
                    Map.class
            );


            Map<String, Object> userAttributes = response.getBody();
            String email = (String) userAttributes.get("email");

            Account account = accountRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = jwtHandler.generateToken(email, account.getRole());

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("User not found"));
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Failed to retrieve user info from Google"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse("Authentication failed"));
        }
    }

    @GetMapping("/oauth2/authorize/google")
    public ResponseEntity<Void> authorizeGoogle(HttpServletResponse response) {
        ClientRegistration clientRegistration = this.clientRegistrationRepository.findByRegistrationId("google");
        if (clientRegistration == null) {
            return ResponseEntity.badRequest().build();
        }

        String authorizationUri = UriComponentsBuilder
                .fromUriString(clientRegistration.getProviderDetails().getAuthorizationUri())
                .queryParam("response_type", "code")
                .queryParam("client_id", clientRegistration.getClientId())
                .queryParam("scope", String.join(" ", clientRegistration.getScopes()))
                .queryParam("redirect_uri", clientRegistration.getRedirectUri())
                .queryParam("state", "state")
                .build().toUriString();

        response.setHeader("Location", authorizationUri);
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

}