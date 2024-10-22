package com.skillspace.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OtpValidationDTO {
    @Schema(example="example@email.com")
    private String email;
    @Schema(example="84251")
    private String otp;
}
