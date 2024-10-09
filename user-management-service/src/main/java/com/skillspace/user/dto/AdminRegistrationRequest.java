package com.skillspace.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRegistrationRequest {
    private String email;
    private String password;
    private String contact;
    private String name;
}
