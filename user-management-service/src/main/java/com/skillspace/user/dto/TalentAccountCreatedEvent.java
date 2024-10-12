package com.skillspace.user.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "type")
public class TalentAccountCreatedEvent {
    private String firstName;
    private String lastName;
    private String email;
    private String activationCode;
}
