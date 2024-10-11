package com.skillspace.user.dto;

import lombok.Data;

@Data
public class TalentAccountCreatedEvent {
    private String firstName;
    private String lastName;
    private String email;
}
