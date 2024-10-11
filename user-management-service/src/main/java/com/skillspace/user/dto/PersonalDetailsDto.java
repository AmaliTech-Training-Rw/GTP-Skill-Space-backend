package com.skillspace.user.dto;

import com.skillspace.user.entity.NotificationPreference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Map;

import java.time.LocalDate;
import java.util.List;


@Data
public class PersonalDetailsDto {

    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,}$", message = "Invalid contact number")
    private String contact;

    private String location;

    private boolean available;

    private boolean contactVisibility;

    @Size(max = 100, message = "Badges cannot exceed 100 characters")
    private String badges;

    private NotificationPreference notificationPreference;

    @URL(message = "Invalid URL format for portfolio")
    private String portfolio;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dob;

    @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
    private String bio;

    private MultipartFile profilePic;

    private Map<@NotBlank(message = "Social media platform cannot be blank") String,
            @URL(message = "Invalid URL for social media link") String> socialMedia;

    private MultipartFile cv;
}
