package com.skillspace.application.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TalentDTO {
    private Long talentId;
    private String firstName;
    private String lastName;
    private LocalDateTime updatedAt;
}