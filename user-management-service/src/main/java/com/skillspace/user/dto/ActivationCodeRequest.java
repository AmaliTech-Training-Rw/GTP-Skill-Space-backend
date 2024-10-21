package com.skillspace.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActivationCodeRequest {
    @NotBlank
    @Email
    @Schema(example="example@email.com")
    private String email;
}
