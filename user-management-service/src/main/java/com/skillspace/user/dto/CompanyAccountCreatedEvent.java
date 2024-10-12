package com.skillspace.user.dto;

import lombok.Data;

@Data
public class CompanyAccountCreatedEvent {
    private String companyName;
    private String companyEmail;
}