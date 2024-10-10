package com.skillspace.career.dto;

import java.util.UUID;

public class CompanyDTO {
    private UUID companyId;
    private String name;

    // Getters and Setters
    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

