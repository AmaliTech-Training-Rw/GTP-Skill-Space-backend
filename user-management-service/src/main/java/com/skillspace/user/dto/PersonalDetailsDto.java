package com.skillspace.user.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class PersonalDetailsDto {

    private Long talentId;

    private String firstName;

    private String lastName;

    private String contact;

    private String location;

    private boolean available;

    private String badges;

    private String notificationPreference;

    private String portfolio;

    private LocalDate dob;

    private String bio;

    private MultipartFile profilePic;

    private Map<String, String> socialMedia;

    private MultipartFile cv;
}
