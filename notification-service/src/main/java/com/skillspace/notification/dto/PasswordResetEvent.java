package com.skillspace.notification.dto;

import lombok.Data;

@Data
public class PasswordResetEvent {
    private String email;
    private String otp;
}
