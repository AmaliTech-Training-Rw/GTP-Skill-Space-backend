package com.skillspace.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TalentRegistrationRequest {
    private String email;
    private String password;
    private String contact;
    private String firstname;
    private String lastname;
}

