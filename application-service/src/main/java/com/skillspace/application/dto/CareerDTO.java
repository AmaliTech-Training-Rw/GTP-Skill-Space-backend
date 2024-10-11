package com.skillspace.application.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CareerDTO {
    private Long id;
    private String name;
    private String description;
    private String status;
    private String startDate;
    private String endDate;
    private List<String> requirements;
    private List<String> requiredBadges;
    private List<String> optionalBadges;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long companyId;
}
