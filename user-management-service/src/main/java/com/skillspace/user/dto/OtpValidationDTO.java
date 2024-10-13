package com.skillspace.user.dto;

import lombok.Data;

@Data
public class OtpValidationDTO {
    private String email;
    private String otp;
}
