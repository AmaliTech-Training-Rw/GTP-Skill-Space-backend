package com.skillspace.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActivationCodeRequest {
    @NotBlank
    @Email
    private String email;
}
