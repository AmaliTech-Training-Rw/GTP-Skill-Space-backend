package com.skillspace.user.dto;

import com.skillspace.user.util.customValidation.ValidContact;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UpdateCompany {
    @ValidContact
    private String contact;

    @Size(max = 50, message = "Company name cannot exceed 100 characters")
    private String name;

    @URL(message = "Invalid URL format for website")
    private String website;
}
