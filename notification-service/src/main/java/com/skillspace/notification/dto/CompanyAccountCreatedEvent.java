package com.skillspace.notification.dto;

import lombok.Data;

@Data
public class CompanyAccountCreatedEvent {
    private String companyName;
    private String companyEmail;
    private String activationCode;
}
