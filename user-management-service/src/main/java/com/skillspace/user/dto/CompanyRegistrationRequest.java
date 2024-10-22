package com.skillspace.user.dto;

import com.skillspace.user.util.customValidation.ValidContact;
import com.skillspace.user.util.customValidation.ValidEmail;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class CompanyRegistrationRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @ValidEmail
    @Schema(example="example@email.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
    )
    @Schema(example="Lg4epo$2b0j!")
    private String password;

    @ValidContact(message = "Invalid contact format")
    @NotBlank(message = "Contact is required")
    @Schema(example="0786534136")
    private String contact;

    @NotBlank(message = "Name is required")
    @Schema(example="Skillspace Company")
    private String name;

//    @NotBlank(message = "Certificate is required")
//    private String certificate;
//
//    @NotBlank(message = "Logo is required")
//    private String logo;

    @Schema(example="https://example.com")
    @URL(message =  "Invalid URL format for website")
    @NotBlank(message = "website link is required")
    private String website;
}
