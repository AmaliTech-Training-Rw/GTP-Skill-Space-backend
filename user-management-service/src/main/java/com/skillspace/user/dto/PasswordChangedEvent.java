package com.skillspace.user.dto;

import lombok.Data;

@Data
public class PasswordChangedEvent {
    private String email;
}
