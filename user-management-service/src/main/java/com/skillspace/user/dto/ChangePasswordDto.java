package com.skillspace.user.dto;

import com.skillspace.user.util.customValidation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {

    @NotBlank(message = "Old password must not be blank")
    private String oldPassword;

    @ValidPassword
    private String newPassword;

    @ValidPassword
    private String confirmNewPassword;
}

