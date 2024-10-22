package com.skillspace.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "OAuth authentication request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuthRequest {
    @Schema(description = "Google ID token", required = true)
    @NotBlank(message = "Token is required")
    private String token;

    @NotBlank(message = "Email is required")
    @Schema(example="example@gmail.com")
    private String email;
}


