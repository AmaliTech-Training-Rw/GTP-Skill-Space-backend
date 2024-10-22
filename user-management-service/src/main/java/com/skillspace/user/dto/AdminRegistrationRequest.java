package com.skillspace.user.dto;

import com.skillspace.user.util.customValidation.ValidContact;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRegistrationRequest {
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Schema(example="example@email.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    @Schema(example="l@YB&RDFVeVJR5")
    private String password;

    @NotBlank(message = "Contact is required")
    @ValidContact(message = "Invalid contact format")
    @Schema(example="0786534136")
    private String contact;

    @NotBlank(message = "Name is required")
    @Schema(example="Peter")
    private String name;
}
