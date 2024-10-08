package com.skillspace.user.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class PersonalDetailsDto {

    private String talentId;

    private String location;

    private boolean available;

    private List<String> badges;

    private String notificationPreference;

    private String portfolio;

    private LocalDate dob;

    private String bio;

    private String profilePic;

    private Map<String, String> socialMedia;

    private String cv;
}
