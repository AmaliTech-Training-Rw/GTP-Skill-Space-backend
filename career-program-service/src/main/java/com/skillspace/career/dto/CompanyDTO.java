package com.skillspace.career.dto;

import java.util.UUID;

public class CompanyDTO {
    private Long companyId;
    private String name;

    // Getters and Setters
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

