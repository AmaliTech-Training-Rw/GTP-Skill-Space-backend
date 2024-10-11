package com.skillspace.user.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.*;

@Table
@Setter
@Getter
public class PersonalDetails {
    @PrimaryKey
    private UUID id;

    @Column("talent_id")
    private Long talentId;

    @Column
    private String location;

    @Column
    private boolean available = true;

    @Column
    private boolean contactVisibility = true;

    @Column
    private List<String> badges;

    @Column("notification_preference")
    private NotificationPreference notificationPreference;

    @Column
    private String portfolio;

    @Column
    private LocalDate dob;

    @Column
    private String bio;

    @Column("profile_pic")
    private String profilePic;

    @Column("social_media")
    private Map<String, String> socialMedia;

    @Column
    private String cv;

}

