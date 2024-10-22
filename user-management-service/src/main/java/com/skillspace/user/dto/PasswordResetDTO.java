package com.skillspace.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PasswordResetDTO {
    @Schema(example="example@email.com")
    private String email;
    @Schema(example="l@YB&RDFVeVJR5")
    private String newPassword;
    @Schema(example="l@YB&RDFVeVJR5")
    private String confirmPassword;
}
