package com.skillspace.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EmailDTO {
    @Schema(example="example@email.com")
    private String email;
}