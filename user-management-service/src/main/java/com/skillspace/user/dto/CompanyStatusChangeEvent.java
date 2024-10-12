package com.skillspace.user.dto;

import lombok.Data;

@Data
public class CompanyStatusChangeEvent {
    private Long companyId;
    private String companyName;
    private String email;
    private String status;
}

