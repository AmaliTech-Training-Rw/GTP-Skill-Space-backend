package com.skillspace.notification.dto;

import lombok.Data;

@Data
public class PasswordChangedEvent {
    private String email;
}
