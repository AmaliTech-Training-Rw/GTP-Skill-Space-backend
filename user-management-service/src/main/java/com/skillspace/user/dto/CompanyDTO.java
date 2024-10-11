package com.skillspace.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyDTO {

    private Long companyId;
    private Long userId;
    private String name;
    private String certificate;
    private String logo;
    private String website;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CompanyDTO(Long companyId, Long userId, String name, String certificate,
                      String logo, String website, LocalDateTime createdAt,
                      LocalDateTime updatedAt) {
        this.companyId = companyId;
        this.userId = userId;
        this.name = name;
        this.certificate = certificate;
        this.logo = logo;
        this.website = website;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}


