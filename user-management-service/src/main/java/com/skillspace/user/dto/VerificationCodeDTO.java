package com.skillspace.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class VerificationCodeDTO {
    @Schema(example="46284")
    private String code;
}
