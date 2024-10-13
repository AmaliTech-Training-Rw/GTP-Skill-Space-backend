package com.skillspace.user.dto;

import lombok.Data;

@Data
public class PasswordResetDTO {
    private String email;
    private String newPassword;
    private String confirmPassword;
}
