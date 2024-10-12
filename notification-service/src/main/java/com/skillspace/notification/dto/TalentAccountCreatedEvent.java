package com.skillspace.notification.dto;

import lombok.Data;

@Data
public class TalentAccountCreatedEvent {
    private String firstName;
    private String lastName;
    private String email;
    private String activationCode;
}
