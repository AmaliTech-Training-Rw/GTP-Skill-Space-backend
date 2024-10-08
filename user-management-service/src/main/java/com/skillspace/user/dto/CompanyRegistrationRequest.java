package com.skillspace.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyRegistrationRequest {
    private String email;
    private String password;
    private String contact;
    private String name;
    private String certificate;
    private String logo;
    private String website;
}
