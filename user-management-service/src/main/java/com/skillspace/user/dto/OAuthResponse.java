package com.skillspace.user.dto;

import com.skillspace.user.entity.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Authentication response")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuthResponse {
    @Schema(description = "JWT token for authenticated user")
    private String jwt;

    @Schema(description = "User email")
    private String email;

    @Schema(description = "User role")
    private UserRole role;
}
