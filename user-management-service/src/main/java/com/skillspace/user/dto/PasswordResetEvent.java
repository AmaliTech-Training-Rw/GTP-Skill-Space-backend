package com.skillspace.user.dto;

import lombok.Data;

@Data
public class PasswordResetEvent {
    private String email;
    private String otp;
}
