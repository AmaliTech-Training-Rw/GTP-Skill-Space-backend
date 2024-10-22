package com.skillspace.user.controller;

import com.skillspace.user.dto.*;
import com.skillspace.user.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class OAuthController {
    @Autowired
    private OAuthService oAuthService;

    @PostMapping("/login/google")
    public ResponseEntity<?> googleLogin(@RequestBody OAuthRequest request) {
        validateToken(request.getToken());

        try {
            OAuthResponse response = oAuthService.authenticateWithGoogle(request.getToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("Authentication failed: " + e.getMessage()));
        }
    }

    public void validateToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token is required");
        }
    }
}
